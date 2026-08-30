package com.den.pos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record UnitRequest(
        @NotBlank @Size(max = 50) String name,
        @Size(max = 10) String abbreviation,
        Boolean isBaseUnit,
        BigDecimal conversionFactor,
        UUID baseUnitId
) {
}
