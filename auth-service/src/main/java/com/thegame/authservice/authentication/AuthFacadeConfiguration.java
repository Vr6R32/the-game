package com.thegame.authservice.authentication;

import com.thegame.authservice.jwt.JwtFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
class AuthFacadeConfiguration {

    public AuthFacadeConfiguration(AuthenticationManager authenticationManager, JwtFacade jwtFacade) {
        this.authenticationManager = authenticationManager;
        this.jwtFacade = jwtFacade;
    }

    private final AuthenticationManager authenticationManager;
    private final JwtFacade jwtFacade;


    @Bean
    public AuthFacade authFacade(){
        AuthService authService = new AuthServiceImpl(authenticationManager,jwtFacade);
        return new AuthFacade(authService);
    }

}
