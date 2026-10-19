package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "RoleResponse", description = "Role with its granted permissions")
public record RoleResponse(
        @Schema(description = "Role UUID")
        UUID id,
        @Schema(description = "Role name", example = "supervisor")
        String name,
        @Schema(description = "Role description", example = "Supervises shifts and can view reports")
        String description,
        @Schema(description = "True for the four seeded roles (owner, admin, manager, cashier); system roles cannot be renamed or deleted", example = "false")
        boolean systemRole,
        @Schema(description = "Granted permission keys (module:action), sorted", example = "[\"products:read\", \"products:create\"]")
        List<String> permissions,
        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt
) {
}
