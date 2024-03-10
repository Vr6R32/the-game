package com.thegame.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final ApiGatewayFilter gatewayFilter;

    public GatewayConfig(ApiGatewayFilter gatewayFilter) {
        this.gatewayFilter = gatewayFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://auth-service"))
                .route("frontend", r -> r.path("/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://frontend"))
                .build();
    }

}