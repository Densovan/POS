package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterRequest", description = "New account details (first user becomes owner, later users become cashier)")
public record RegisterRequest(
        @Schema(description = "Account email", example = "cashier@pos.local")
        @NotBlank @Email String email,
        @Schema(description = "Password, 8-100 characters", example = "Cashier@123")
        @NotBlank @Size(min = 8, max = 100) String password) {
}
