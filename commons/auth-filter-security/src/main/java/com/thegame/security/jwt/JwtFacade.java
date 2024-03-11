package com.thegame.security.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.server.ServerWebExchange;

public record JwtFacade(JwtService jwtService) {

    public TokenResponse authenticate(AppUser user, HttpServletResponse response) {
        return jwtService.authenticate(user, response);
    }

    public AuthenticationUserObject authenticateAccessToken(String token){
        return jwtService.authenticateAccessToken(token);
    }

    public AuthenticationUserObject authenticateRefreshToken(String token, ServerWebExchange response) {
        return jwtService.authenticateRefreshToken(token, response);
    }
}
