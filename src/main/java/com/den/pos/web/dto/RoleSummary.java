package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "RoleSummary", description = "Role reference embedded in user responses")
public record RoleSummary(
        @Schema(description = "Role UUID")
        UUID id,
        @Schema(description = "Role name", example = "cashier")
        String name
) {
}
