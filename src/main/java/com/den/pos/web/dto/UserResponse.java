package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "UserResponse", description = "User account with assigned roles (credentials are never returned)")
public record UserResponse(
        @Schema(description = "User UUID")
        UUID id,
        @Schema(description = "Login email", example = "cashier@pos.local")
        String email,
        @Schema(description = "Whether the account can log in", example = "true")
        Boolean isActive,
        @Schema(description = "Last successful login")
        LocalDateTime lastLoginAt,
        @Schema(description = "Assigned roles")
        List<RoleSummary> roles
) {
}
