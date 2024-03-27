package com.thegame.authservice.authentication;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.thegame.dto.AuthenticationUserObject;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"message", "httpStatus", "accessToken", "refreshToken","user"})
record AuthResponse(String message, HttpStatus httpStatus, String accessToken, String refreshToken, AuthenticationUserObject user) {
}
