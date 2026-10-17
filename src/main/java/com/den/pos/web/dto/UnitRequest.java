package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "UnitRequest", description = "Unit of measure create/update payload")
public record UnitRequest(
        @Schema(description = "Unit name", example = "Kilogram")
        @NotBlank @Size(max = 50) String name,
        @Schema(description = "Short abbreviation", example = "kg")
        @Size(max = 10) String abbreviation,
        @Schema(description = "True for base units (pcs, kg, L)", example = "true")
        Boolean isBaseUnit,
        @Schema(description = "How many base units this unit equals", example = "1.00")
        BigDecimal conversionFactor,
        @Schema(description = "Optional base unit UUID (required when isBaseUnit is false)")
        UUID baseUnitId
) {
}
