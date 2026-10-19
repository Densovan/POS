package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

@Schema(name = "UserRolesRequest", description = "Full replacement set of roles for a user")
public record UserRolesRequest(
        @ArraySchema(schema = @Schema(example = "0192c1f4-0000-7000-8000-000000000000"))
        @Schema(description = "Every role the user should have; any not listed is revoked. Copy ids from GET /roles")
        @NotEmpty
        List<UUID> roleIds
) {
}
