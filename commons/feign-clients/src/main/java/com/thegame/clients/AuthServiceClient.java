package com.thegame.clients;


import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @PostMapping("api/v1/auth/validate-token")
    AuthenticationUserObject validateToken(@RequestParam(name = "accessToken") String accessToken);

    @PostMapping("api/v1/auth/refresh-token")
    RefreshTokenAuthResponse refreshToken(@RequestParam(name = "refreshToken") String refreshToken);

}