package com.thegame.jwt;

public record AuthenticationResponse(String accessToken, String refreshToken) {
}
