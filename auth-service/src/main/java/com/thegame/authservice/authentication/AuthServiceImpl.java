package com.thegame.authservice.authentication;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.authservice.jwt.AuthenticationResponse;
import com.thegame.authservice.jwt.JwtFacade;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


@Slf4j
record AuthServiceImpl(AuthenticationManager authenticationManager, JwtFacade jwtFacade) implements AuthService {


    @Override
    public AuthResponse authenticate(AuthRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
            AppUser user = (AppUser) authentication.getPrincipal();

            AuthenticationUserObject responseUserObject = AuthenticationUserObject.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();

            AuthenticationResponse authenticationResponse = jwtFacade.authenticate(user, response);
            return new AuthResponse("AUTHENTICATED",HttpStatus.OK, authenticationResponse.accessToken(), authenticationResponse.refreshToken(), responseUserObject);
        } catch (AuthenticationException e) {
            log.info("[AUTHENTICATION-SERVICE] -> {} FOR {} ",e.getMessage(),request);
            return new AuthResponse(e.getMessage(), HttpStatus.UNAUTHORIZED,null,null,null);
        }
    }

    @Override
    public LogoutResponse logout(HttpServletResponse response) {
        return jwtFacade.logout(response);
    }

    @Override
    public AuthenticationUserObject validateToken(String accessToken) {
        return jwtFacade.authenticateAccessToken(accessToken);
    }

    @Override
    public RefreshTokenAuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        return jwtFacade.authenticateRefreshToken(refreshToken, response);
    }
}
