package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "Login credentials")
public record LoginRequest(
        @Schema(description = "Account email", example = "admin@pos.local")
        @NotBlank @Email String email,
        @Schema(description = "Account password", example = "Admin@123")
        @NotBlank String password) {
}
