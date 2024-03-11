package com.thegame.config;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class RouteValidator {

    private final List<String> openEndpoints = List.of(
            "/api/v1/users/generate",
            "/api/v1/auth/logout",
            "/api/v1/auth/login",
            "/login",
            "/"
    );

    public boolean isSecured(ServerHttpRequest request) {
        return openEndpoints.stream()
                .noneMatch(e -> request.getURI().getPath().equals(e));
    }
}