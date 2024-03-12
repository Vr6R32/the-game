package com.thegame.security.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.server.ServerWebExchange;

interface JwtService {

    TokenResponse authenticate(AppUser user, HttpServletResponse response);

    AuthenticationUserObject authenticateAccessToken(String accessToken);

    AuthenticationUserObject authenticateRefreshToken(String token, ServerWebExchange response);

    LogoutResponse logout(HttpServletResponse response);
}
