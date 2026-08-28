package com.den.pos.web.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds) {

    public static TokenResponse of(String accessToken, String refreshToken, long expirationMs) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", expirationMs / 1000);
    }
}
