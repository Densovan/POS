package com.den.pos.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UnitResponse(
        UUID id,
        String name,
        String abbreviation,
        Boolean isBaseUnit,
        BigDecimal conversionFactor,
        UUID baseUnitId,
        LocalDateTime createdAt
) {
}
