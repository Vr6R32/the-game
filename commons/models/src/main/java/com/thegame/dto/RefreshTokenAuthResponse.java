package com.thegame.dto;

import lombok.Builder;
import org.springframework.http.HttpHeaders;


@Builder
public record RefreshTokenAuthResponse(AuthenticationUserObject authenticationUserObject, HttpHeaders httpHeaders) {
}
