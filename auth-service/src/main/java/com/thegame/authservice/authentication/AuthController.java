package com.thegame.authservice.authentication;

import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
record AuthController(AuthFacade authFacade) {


    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest request, HttpServletResponse response) {
        return authFacade.authenticate(request, response);
    }

    @GetMapping("/logout")
    public LogoutResponse logout(HttpServletResponse response) {
        return authFacade.logout(response);
    }


    @PostMapping("/validate-token")
    public AuthenticationUserObject validateToken(@RequestParam(name = "accessToken") String accessToken) {
        return authFacade.validateToken(accessToken);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenAuthResponse refreshToken(@RequestParam(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        return authFacade.refreshToken(refreshToken,response);
    }

}
