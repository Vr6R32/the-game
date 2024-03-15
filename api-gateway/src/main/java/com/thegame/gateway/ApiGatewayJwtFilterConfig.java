package com.thegame.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.security.jwt.JwtFacade;
import com.thegame.security.jwt.TokenEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ApiGatewayJwtFilterConfig {

    private final JwtFacade jwtFacade;
    private final ObjectMapper objectMapper;

    @Bean
    ApiGatewayJwtFilter gatewayFilter(){
        TokenEncryption tokenEncryption = new TokenEncryption();
        RouteValidator routeValidator = new RouteValidator();
        return new ApiGatewayJwtFilter(routeValidator,tokenEncryption,jwtFacade,objectMapper);
    }
}
