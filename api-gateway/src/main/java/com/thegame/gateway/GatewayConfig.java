package com.thegame.gateway;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;


@Configuration
public class GatewayConfig {

    private final ApiGatewayJwtFilter gatewayFilter;

    public GatewayConfig(ApiGatewayJwtFilter gatewayFilter) {
        this.gatewayFilter = gatewayFilter;
    }

    @Bean
    @Primary
    public RequestUpgradeStrategy requestUpgradeStrategy() {
        return new TomcatRequestUpgradeStrategy();
    }
    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new CustomErrorWebExceptionHandler();
    }

    @Bean
    public GatewayFilter corsFilter() {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            headers.set("Content-Security-Policy", "frame-ancestors 'self' http://localhost:8083");
            return chain.filter(exchange);
        };
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("messaging-service-sockjs", r -> r.path("/websocket/**")
//                        .filters(f -> f.filter(webSocketHeaderFilters)
//                                .filter(gatewayFilter)
//                                .filter(corsFilter()))
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://MESSAGING-SERVICE"))
                .route("messaging-service-websocket", r -> r.path("/chat/**")
//                        .filters(f -> f.filter(webSocketHeaderFilters)
//                                .filter(gatewayFilter)
//                                .filter(corsFilter()))
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb:ws://MESSAGING-SERVICE"))
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://USER-SERVICE"))

                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://AUTH-SERVICE"))

                .route("robbery-service", r -> r.path("/api/v1/robberies/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://ROBBERIES-SERVICE"))

                .route("frontend", r -> r.path("/messages")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://FRONTEND"))

                .route("frontend", r -> r.path("/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://FRONTEND"))
                .build();
    }
}