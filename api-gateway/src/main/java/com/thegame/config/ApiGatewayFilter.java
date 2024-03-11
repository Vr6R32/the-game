package com.thegame.config;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.security.JwtConfig;
import com.thegame.security.jwt.JwtService;
import com.thegame.security.jwt.JwtServiceImpl;
import com.thegame.security.jwt.TokenEncryption;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class ApiGatewayFilter implements GatewayFilter {

    private final RouteValidator routeValidator = new RouteValidator();
    private final JwtConfig jwtConfig = new JwtConfig();
    private final TokenEncryption tokenEncryption = new TokenEncryption(jwtConfig);
    private final JwtService jwtService = new JwtServiceImpl(tokenEncryption,jwtConfig);


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
            System.out.println(decryptedAccessToken);

            AuthenticationUserObject appUser = jwtService.resolveToken(decryptedAccessToken);
            System.out.println(appUser);
            return chain.filter(exchange);
        }
        System.out.println("no token - secured");
        return onError(exchange, HttpStatus.FORBIDDEN);
    }
}