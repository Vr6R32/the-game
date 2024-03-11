package com.thegame.authentication;

import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

record AuthFacade(AuthService authService) {

    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {
        return authService.authenticate(request,response);
    }

    public LogoutResponse logout(HttpServletResponse response) {
        return authService.logout(response);
    }
}
