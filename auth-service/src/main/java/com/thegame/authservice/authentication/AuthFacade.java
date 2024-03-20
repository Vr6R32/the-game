package com.thegame.authservice.authentication;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

record AuthFacade(AuthService authService) {

    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {
        return authService.authenticate(request,response);
    }

    public LogoutResponse logout(HttpServletResponse response) {
        return authService.logout(response);
    }

    public AuthenticationUserObject validateToken(String accessToken) {
        return authService.validateToken(accessToken);
    }

    public RefreshTokenAuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        return authService.refreshToken(refreshToken,response);
    }
}
