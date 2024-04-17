package com.thegame.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public class RouteValidator {

        private final List<String> openEndpoints = List.of(
                "/api/v1/users/register",
                "/api/v1/auth/login",
                "/favicon.ico",
                "/messages",
                "/logout",
                "/login",
                "/test",
                "/"
        );

    /**
     *
     * THIS LIST TAKES URL WITH GLOBAL PREFIXES LIKE
     * FOR EXAMPLE   /css/**  , /jss/** ,
     * DO NOT ADD EXTRA /** PREFIX ITS UNNECESSARY
     *
     */

    private final List<String> openEndpointPrefixes = List.of(
//                "/websocket/",
                "/api/v1/auth/refresh-token/",
                "/api/v1/static/",
                "/css/",
                "/js/"
        );

        public boolean isSecured(ServerHttpRequest request) {
            String path = request.getURI().getPath();
            boolean isExactMatch = openEndpoints.stream().anyMatch(path::equals);
            if (isExactMatch) {
                return false;
            }
            boolean isPrefixMatch = openEndpointPrefixes.stream().anyMatch(path::startsWith);
            return !isPrefixMatch;
    }
}
