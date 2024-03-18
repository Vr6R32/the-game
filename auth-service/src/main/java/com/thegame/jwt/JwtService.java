package com.thegame.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;

interface JwtService {

    AuthenticationResponse authenticate(AppUser user, HttpServletResponse response);

    AuthenticationUserObject authenticateToken(String accessToken);

    RefreshTokenAuthResponse authenticateRefreshToken(String refreshToken, HttpServletResponse response);

    LogoutResponse logout(HttpServletResponse response);
}
