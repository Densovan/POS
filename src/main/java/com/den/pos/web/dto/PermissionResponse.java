package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "PermissionResponse", description = "One entry of the permission catalogue")
public record PermissionResponse(
        @Schema(description = "Permission UUID")
        UUID id,
        @Schema(description = "Module the permission belongs to", example = "products")
        String module,
        @Schema(description = "Action within the module", example = "read")
        String action,
        @Schema(description = "Authority string checked by @PreAuthorize", example = "products:read")
        String key,
        @Schema(description = "Human-readable meaning", example = "View products")
        String description
) {
}
