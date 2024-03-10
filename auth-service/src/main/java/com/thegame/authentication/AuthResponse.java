package com.thegame.authentication;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@JsonPropertyOrder({"message", "httpStatus", "accessToken", "refreshToken"})
record AuthResponse(String message, HttpStatus httpStatus, String accessToken, String refreshToken) {
}
