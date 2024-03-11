package com.thegame.security.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import jakarta.servlet.http.HttpServletResponse;

public record JwtFacade(JwtService jwtService) {

    public TokenResponse authenticate(AppUser user, HttpServletResponse response) {
        return jwtService.authenticate(user, response);
    }

    public AuthenticationUserObject resolveToken(String token){
        return jwtService.resolveToken(token);
    }
}
