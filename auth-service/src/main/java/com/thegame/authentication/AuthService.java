package com.thegame.authentication;

import jakarta.servlet.http.HttpServletResponse;

interface AuthService {

    AuthResponse authenticate(AuthRequest request, HttpServletResponse response);

}
