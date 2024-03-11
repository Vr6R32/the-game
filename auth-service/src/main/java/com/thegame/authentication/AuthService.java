package com.thegame.authentication;

import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

interface AuthService {

    AuthResponse authenticate(AuthRequest request, HttpServletResponse response);

    LogoutResponse logout(HttpServletResponse response);
}
