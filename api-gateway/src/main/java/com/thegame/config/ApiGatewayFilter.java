package com.thegame.config;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.security.jwt.JwtFacade;
import com.thegame.security.jwt.TokenEncryption;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class ApiGatewayFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final TokenEncryption tokenEncryption;
    private final JwtFacade jwtFacade;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routeValidator.isSecured(request)) {
            if (areHttpCookieTokensMissing(request)) {
                return processBearerTokenAuthorization(exchange, chain);
            } else {
                return processHttpCookieAuthorization(exchange, chain);
            }
        } else {
            return chain.filter(exchange);
        }
    }

    private Mono<Void> processHttpCookieAuthorization(ServerWebExchange exchange, GatewayFilterChain chain) {
        RequestCookies requestCookies = RequestCookies.extractCookiesFromRequest(exchange.getRequest().getCookies());
        if (requestCookies.accessToken() != null && !requestCookies.accessToken().isEmpty()) {
            String accessToken = requestCookies.accessToken();
            return authenticateToken(exchange, chain, accessToken);
        } else if (requestCookies.refreshToken() != null && !requestCookies.refreshToken().isEmpty()) {
            String refreshToken = requestCookies.refreshToken();
            return authenticateRefreshToken(exchange, chain, refreshToken);
        }
        log.info("missing httpCookie token - secured");
        return onError(exchange, HttpStatus.FORBIDDEN);
    }

    private Mono<Void> authenticateRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain, String refreshToken) {
        String decryptedRefreshToken = tokenEncryption.decrypt(refreshToken);

        AuthenticationUserObject appUser = null;

        try {
            appUser = jwtFacade.authenticateRefreshToken(decryptedRefreshToken,exchange);

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-USER-AUTH", appUser.toString())
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();

            return chain.filter(newExchange);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.warn(e.getMessage() + appUser);
            return onError(exchange, HttpStatus.FORBIDDEN);
        }
    }

    private Mono<Void> processBearerTokenAuthorization(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authHeaderList = exchange.getRequest().getHeaders().get("Authorization");

        if (authHeaderList != null && !authHeaderList.isEmpty()) {
            String bearerHeader = authHeaderList.get(0);
            String token = bearerHeader.substring(7);
            return authenticateToken(exchange, chain, token);
        }
        log.info("missing bearer token - secured");
        return onError(exchange, HttpStatus.FORBIDDEN);
    }

    private Mono<Void> authenticateToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        String decryptedAccessToken = tokenEncryption.decrypt(token);
        AuthenticationUserObject appUser = null;
        try {
            appUser = jwtFacade.authenticateAccessToken(decryptedAccessToken);

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-USER-AUTH", appUser.toString())
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();

            return chain.filter(newExchange);
        } catch (ExpiredJwtException e) {
            return processHttpCookieAuthorization(exchange, chain);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.warn(e.getMessage() + appUser);
            return onError(exchange, HttpStatus.FORBIDDEN);
            }
        }

        private boolean areHttpCookieTokensMissing (ServerHttpRequest request){
            RequestCookies requestCookies = RequestCookies.extractCookiesFromRequest(request.getCookies());
            return requestCookies.accessToken() == null && requestCookies.refreshToken() == null;
        }

        private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(httpStatus);
            return response.setComplete();
        }

}