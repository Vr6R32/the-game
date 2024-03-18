package com.thegame.authentication;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

interface AuthService {

    AuthResponse authenticate(AuthRequest request, HttpServletResponse response);

    LogoutResponse logout(HttpServletResponse response);

    AuthenticationUserObject validateToken(String accessToken);

    RefreshTokenAuthResponse refreshToken(String refreshToken, HttpServletResponse response);
}
