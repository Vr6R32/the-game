package com.thegame.security.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {

    TokenResponse authenticate(AppUser user, HttpServletResponse response);

    AuthenticationUserObject resolveToken(String accessToken);

}
