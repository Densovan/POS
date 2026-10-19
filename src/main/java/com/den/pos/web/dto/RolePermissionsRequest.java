package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Schema(name = "RolePermissionsRequest", description = "Full replacement set of permissions for a role, as module:action keys")
public record RolePermissionsRequest(
        @ArraySchema(schema = @Schema(example = "products:read"))
        @Schema(description = "Every permission the role should have; any not listed is revoked. Copy keys from GET /roles/permissions")
        @NotEmpty
        List<@NotBlank @Pattern(regexp = "^[a-z]+:[a-z_]+$", message = "must look like module:action") String> permissions
) {
}
