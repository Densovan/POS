package com.den.pos.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String sku,
        String barcode,
        UUID categoryId,
        UUID brandId,
        UUID unitId,
        UUID taxClassId,
        BigDecimal costPrice,
        BigDecimal sellingPrice,
        BigDecimal wholesalePrice,
        BigDecimal profitMargin,
        Integer minStockLevel,
        Integer reorderPoint,
        String shortDescription,
        String description,
        String mainImageUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID createdBy,
        UUID updatedBy,
        AuditUserRef createdByUser,
        AuditUserRef updatedByUser
) {
}
