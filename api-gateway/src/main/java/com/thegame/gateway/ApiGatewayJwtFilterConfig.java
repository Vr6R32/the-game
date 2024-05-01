package com.thegame.gateway;

import com.thegame.clients.AuthServiceClient;
import com.thegame.mapper.AuthMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ComponentScan("com.thegame.mapper")
class ApiGatewayJwtFilterConfig {

    private final AuthMapper authMapper;
    private final AuthServiceClient authServiceClient;


    public ApiGatewayJwtFilterConfig(AuthMapper authMapper, @Lazy AuthServiceClient authServiceClient) {
        this.authMapper = authMapper;
        this.authServiceClient = authServiceClient;
    }

    @Bean
    ApiGatewayJwtFilter gatewayFilter(){
        RouteValidator routeValidator = new RouteValidator();
        return new ApiGatewayJwtFilter(routeValidator,authMapper,authServiceClient);
    }
}
