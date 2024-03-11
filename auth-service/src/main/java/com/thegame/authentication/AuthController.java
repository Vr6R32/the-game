package com.thegame.authentication;

import com.thegame.response.LogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
record AuthController(AuthFacade authFacade) {

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest request, HttpServletResponse response){
        return authFacade.authenticate(request,response);
    }


    @GetMapping("/logout")
    public LogoutResponse logout(HttpServletResponse response) {
        return authFacade.logout(response);
    }

}
