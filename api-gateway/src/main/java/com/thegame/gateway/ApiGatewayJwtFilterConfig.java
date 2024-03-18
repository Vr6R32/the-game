package com.thegame.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.clients.AuthServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
class ApiGatewayJwtFilterConfig {

    private final ObjectMapper objectMapper;
    private final AuthServiceClient authServiceClient;


    public ApiGatewayJwtFilterConfig(ObjectMapper objectMapper, @Lazy AuthServiceClient authServiceClient) {
        this.objectMapper = objectMapper;
        this.authServiceClient = authServiceClient;
    }

    @Bean
    ApiGatewayJwtFilter gatewayFilter(){
        RouteValidator routeValidator = new RouteValidator();
        return new ApiGatewayJwtFilter(routeValidator,objectMapper,authServiceClient);
    }
}
