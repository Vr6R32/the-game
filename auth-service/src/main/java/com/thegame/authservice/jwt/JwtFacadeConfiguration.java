package com.thegame.authservice.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JwtFacadeConfiguration {

    public JwtFacadeConfiguration(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private final JwtConfig jwtConfig;

    @Bean
    public JwtFacade jwtFacade(){
        TokenEncryption tokenEncryption = new TokenEncryption(jwtConfig);
        JwtService jwtService = new JwtServiceImpl(tokenEncryption,jwtConfig);
        return new JwtFacade(jwtService);
    }

}