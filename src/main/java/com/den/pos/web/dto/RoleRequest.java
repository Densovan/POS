package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "RoleRequest", description = "Role create/update payload")
public record RoleRequest(
        @Schema(description = "Role name (lowercase letters, digits, '-' or '_')", example = "supervisor")
        @NotBlank
        @Size(max = 50)
        @Pattern(regexp = "^[a-z][a-z0-9_-]*$", message = "must be lowercase letters, digits, '-' or '_'")
        String name,
        @Schema(description = "What the role is for", example = "Supervises shifts and can view reports")
        @Size(max = 500)
        String description
) {
}
