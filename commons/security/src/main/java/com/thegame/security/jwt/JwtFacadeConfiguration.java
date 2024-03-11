package com.thegame.security.jwt;

import com.thegame.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class JwtFacadeConfiguration {

    private final JwtConfig jwtConfig;


    @Bean
    public JwtFacade jwtFacade(){
        TokenEncryption tokenEncryption = new TokenEncryption(jwtConfig);
        JwtService jwtService = new JwtServiceImpl(tokenEncryption,jwtConfig);
        return new JwtFacade(jwtService);
    }

}