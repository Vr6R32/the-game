package com.thegame.authservice.jwt;

public record AuthenticationResponse(String accessToken, String refreshToken) {
}
