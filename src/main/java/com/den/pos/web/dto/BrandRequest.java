package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "BrandRequest", description = "Brand create/update payload")
public record BrandRequest(
        @Schema(description = "Brand name", example = "Lavazza")
        @NotBlank @Size(max = 100)
        String name,
        @Schema(description = "Logo image URL", example = "https://example.com/logos/lavazza.png")
        String logoUrl,
        @Schema(description = "Official website", example = "https://www.lavazza.com")
        String websiteUrl,
        @Schema(description = "Whether the brand is active", example = "true")
        Boolean isActive
) {
}
