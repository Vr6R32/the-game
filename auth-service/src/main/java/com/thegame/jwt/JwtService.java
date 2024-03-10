package com.thegame.jwt;

import com.thegame.AppUser;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {

    TokenResponse authenticate(AppUser user, HttpServletResponse response);

}
