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
                        .uri("lb://USER-SERVICE"))
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://AUTH-SERVICE"))
                .route("auth-service", r -> r.path("/api/v1/robberies/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://ROBBERIES-SERVICE"))
                .route("frontend", r -> r.path("/**")
                        .filters(f -> f.filter(gatewayFilter))
                        .uri("lb://FRONTEND"))
                .build();
    }

}