package com.thegame.config;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class RouteValidator {

    private RouteValidator() {
    }

    private static final List<String> openEndpoints = List.of(
            "/api/v1/auth",
            "/"
    );

    public static boolean isSecured(ServerHttpRequest request) {
        return openEndpoints.stream()
                .noneMatch(uri -> request.getURI().getPath().contains(uri));
    }
}