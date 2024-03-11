package com.thegame.authentication;

import com.thegame.security.jwt.JwtFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@RequiredArgsConstructor
class AuthFacadeConfiguration {

    private final AuthenticationManager authenticationManager;
    private final JwtFacade jwtFacade;


    @Bean
    public AuthFacade authFacade(){
        AuthService authService = new AuthServiceImpl(authenticationManager,jwtFacade);
        return new AuthFacade(authService);
    }

}
