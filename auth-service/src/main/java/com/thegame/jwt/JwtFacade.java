package com.thegame.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

public record JwtFacade(JwtService jwtService) {

    public AuthenticationResponse authenticate(AppUser user, HttpServletResponse response) {
        return jwtService.authenticate(user, response);
    }

    public AuthenticationUserObject authenticateAccessToken(String accessToken){
        return jwtService.authenticateToken(accessToken);
    }

    public RefreshTokenAuthResponse authenticateRefreshToken(String refreshToken, HttpServletResponse response) {
        return jwtService.authenticateRefreshToken(refreshToken, response);
    }

    public LogoutResponse logout(HttpServletResponse response) {
        return jwtService.logout(response);
    }
}
