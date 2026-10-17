package com.den.pos.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaxClassResponse(
        UUID id,
        String name,
        BigDecimal rate,
        String description,
        Boolean isDefault,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID createdBy,
        UUID updatedBy,
        AuditUserRef createdByUser,
        AuditUserRef updatedByUser
) {
}
