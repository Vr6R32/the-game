package com.thegame.authentication;

import jakarta.servlet.http.HttpServletResponse;

record AuthFacade(AuthService authService) {

    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {
        return authService.authenticate(request,response);
    }
}
