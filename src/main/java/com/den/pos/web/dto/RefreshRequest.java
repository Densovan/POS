package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RefreshRequest", description = "Refresh token returned by /auth/login or /auth/refresh")
public record RefreshRequest(
        @Schema(description = "The refresh token issued at login", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBwb3MubG9jYWwiLCJ0eXAiOiJyZWZyZXNoIn0.example")
        @NotBlank String refreshToken) {
}
