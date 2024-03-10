package com.thegame.authentication;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
record AuthController(AuthFacade authFacade) {

    @PostMapping
    public AuthResponse authenticate(@RequestBody AuthRequest request, HttpServletResponse response){
        return authFacade.authenticate(request,response);
    }

}
