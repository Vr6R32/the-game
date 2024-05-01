package com.thegame.gateway;

import com.thegame.clients.AuthServiceClient;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.mapper.AuthMapper;
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
class ApiGatewayJwtFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final AuthMapper authMapper;
    private final AuthServiceClient authServiceClient;

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
        return missingTokenResponse(exchange, HttpStatus.FORBIDDEN);
    }

    private Mono<Void> authenticateRefreshToken(ServerWebExchange exchange, GatewayFilterChain chain, String refreshToken) {

            RefreshTokenAuthResponse appUser = authServiceClient.refreshToken(refreshToken);
            exchange.getResponse().getHeaders().addAll(appUser.httpHeaders());
            String jsonUserAuthObject = authMapper.mapUserToJsonObject(appUser.authenticationUserObject());

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-USER-AUTH", jsonUserAuthObject)
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();

            return chain.filter(newExchange);
    }

    private Mono<Void> processBearerTokenAuthorization(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authHeaderList = exchange.getRequest().getHeaders().get("Authorization");

        if (authHeaderList != null && !authHeaderList.isEmpty()) {
            String bearerHeader = authHeaderList.get(0);
            String token = bearerHeader.substring(7);
            return authenticateToken(exchange, chain, token);
        }
        log.info("missing bearer token - secured");
        return missingTokenResponse(exchange, HttpStatus.FORBIDDEN);
    }

    private Mono<Void> authenticateToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {

            AuthenticationUserObject appUser = authServiceClient.validateToken(token);
            String jsonUserAuthObject = authMapper.mapUserToJsonObject(appUser);

            ServerHttpRequest request = exchange.getRequest()
                    .mutate()
                    .header("X-USER-AUTH", jsonUserAuthObject)
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(request).build();

            return chain.filter(newExchange);
        }


    private boolean areHttpCookieTokensMissing (ServerHttpRequest request){
            RequestCookies requestCookies = RequestCookies.extractCookiesFromRequest(request.getCookies());
            return requestCookies.accessToken() == null && requestCookies.refreshToken() == null;
        }

        private Mono<Void> missingTokenResponse(ServerWebExchange exchange, HttpStatus httpStatus){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(httpStatus);
            return response.setComplete();
        }

}