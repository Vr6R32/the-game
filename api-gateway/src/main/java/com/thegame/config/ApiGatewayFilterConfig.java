package com.thegame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.security.jwt.JwtFacade;
import com.thegame.security.jwt.TokenEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ApiGatewayFilterConfig {

    private final JwtFacade jwtFacade;
    private final ObjectMapper objectMapper;

    @Bean
    ApiGatewayFilter gatewayFilter(){
        TokenEncryption tokenEncryption = new TokenEncryption();
        RouteValidator routeValidator = new RouteValidator();
        return new ApiGatewayFilter(routeValidator,tokenEncryption,jwtFacade,objectMapper);
    }
}