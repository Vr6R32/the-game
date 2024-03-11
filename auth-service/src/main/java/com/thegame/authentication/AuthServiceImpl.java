package com.thegame.authentication;

import com.thegame.AppUser;
import com.thegame.response.LogoutResponse;
import com.thegame.security.jwt.JwtFacade;
import com.thegame.security.jwt.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


@Slf4j
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtFacade jwtFacade;

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
            TokenResponse tokenResponse = jwtFacade.authenticate(user, response);
            return new AuthResponse("AUTHENTICATED",HttpStatus.OK, tokenResponse.accessToken(), tokenResponse.refreshToken());
        } catch (AuthenticationException e) {
            log.info("[AUTHENTICATION-SERVICE] -> {} FOR {} ",e.getMessage(),request);
            return new AuthResponse(e.getMessage(), HttpStatus.UNAUTHORIZED,null,null);
        }
    }

    @Override
    public LogoutResponse logout(HttpServletResponse response) {
        return jwtFacade.logout(response);
    }
}
