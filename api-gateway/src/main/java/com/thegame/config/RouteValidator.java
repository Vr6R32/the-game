package com.thegame.config;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class RouteValidator {

    public RouteValidator() {
        // TODO document why this constructor is empty
    }

    private final List<String> openEndpoints = List.of(
            "/api/v1/auth",
            "/"
    );

    public boolean isSecured(ServerHttpRequest request) {
        return openEndpoints.stream()
                .noneMatch(e -> request.getURI().getPath().equals(e));
    }
}