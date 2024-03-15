package com.thegame.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class WebSocketHeaderFilters implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(builder -> {
                    HttpHeaders existingHeaders = exchange.getRequest().getHeaders();
                    HttpHeaders newHeaders = new HttpHeaders();
                    existingHeaders.forEach((key, value) -> {
                        if (!newHeaders.containsKey(key)) {
                            newHeaders.addAll(key, value);
                        }
                    });
                    builder.headers(httpHeaders -> httpHeaders.putAll(newHeaders));
                })
                .build();
        return chain.filter(modifiedExchange);
    }
}