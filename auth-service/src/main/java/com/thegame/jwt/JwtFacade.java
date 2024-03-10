package com.thegame.jwt;

import com.thegame.AppUser;
import jakarta.servlet.http.HttpServletResponse;

public record JwtFacade(JwtService jwtService) {

    public TokenResponse authenticate(AppUser user, HttpServletResponse response) {
        return jwtService.authenticate(user, response);
    }
}
