package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(name = "CategoryRequest", description = "Category create/update payload")
public record CategoryRequest(
        @Schema(description = "Optional parent category UUID (omit for a root category)")
        UUID parentId,
        @Schema(description = "Category name", example = "Beverages")
        @NotBlank @Size(max = 100) String name,
        @Schema(description = "Category description", example = "Coffee, tea and soft drinks")
        @Size(max = 10000) String description,
        @Schema(description = "Category image URL", example = "https://example.com/images/beverages.jpg")
        String imageUrl,
        @Schema(description = "Optional default tax class UUID")
        UUID taxClassId,
        @Schema(description = "Whether the category is active", example = "true")
        Boolean isActive,
        @Schema(description = "Sort order in listings", example = "1")
        Integer displayOrder
) {
}
