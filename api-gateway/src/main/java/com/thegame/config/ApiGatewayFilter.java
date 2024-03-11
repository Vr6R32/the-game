package com.thegame.config;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.security.jwt.JwtFacade;
import com.thegame.security.jwt.TokenEncryption;
import io.jsonwebtoken.ExpiredJwtException;
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
            }
            System.out.println("Auth failed secured");
            return onError(exchange, HttpStatus.FORBIDDEN);
        } else {
            System.out.println("not secured");
            return chain.filter(exchange);
        }
    }

    private boolean areHttpCookieTokensMissing(ServerHttpRequest request) {
        RequestCookies requestCookies = RequestCookies.extractCookiesFromRequest(request.getCookies());
        return requestCookies.accessToken() == null && requestCookies.refreshToken() == null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private Mono<Void> processBearerTokenAuthorization(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authHeaderList = exchange.getRequest().getHeaders().get("Authorization");

        if (authHeaderList != null && !authHeaderList.isEmpty()) {
            String bearerHeader = authHeaderList.get(0);
            String token = bearerHeader.substring(7);
            String decryptedAccessToken = tokenEncryption.decrypt(token);
            try {
                AuthenticationUserObject appUser = jwtFacade.resolveToken(decryptedAccessToken);

                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .header("X-USER-AUTH", appUser.toString())
                        .build();
                ServerWebExchange newExchange = exchange.mutate().request(request).build();

                return chain.filter(newExchange);
            } catch (ExpiredJwtException e) {
                log.warn(e.getMessage());
                return onError(exchange, HttpStatus.FORBIDDEN);
            }
        }
        System.out.println("no token - secured");
        return onError(exchange, HttpStatus.FORBIDDEN);
    }
}