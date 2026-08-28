package com.den.pos.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 100) String sku,
        @Size(max = 100) String barcode,
        UUID categoryId,
        UUID brandId,
        UUID unitId,
        UUID taxClassId,
        @NotNull @DecimalMin("0.0") BigDecimal costPrice,
        @NotNull @DecimalMin("0.0") BigDecimal sellingPrice,
        BigDecimal wholesalePrice,
        Integer minStockLevel,
        Integer maxStockLevel,
        Integer reorderPoint,
        Integer reorderQuantity,
        @Size(max = 500) String shortDescription,
        String description,
        String mainImageUrl
) {
}
