package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "ProductRequest", description = "Product create/update payload")
public record ProductRequest(
        @Schema(description = "Product name", example = "Espresso Beans 1kg")
        @NotBlank @Size(max = 255) String name,
        @Schema(description = "Unique stock keeping unit", example = "BEAN-ESP-1KG")
        @NotBlank @Size(max = 100) String sku,
        @Schema(description = "Barcode (EAN/UPC/CODE128)", example = "8851001000015")
        @Size(max = 100) String barcode,
        @Schema(description = "Optional category UUID — copy a real id from GET /categories")
        UUID categoryId,
        @Schema(description = "Optional brand UUID — copy a real id from GET /brands")
        UUID brandId,
        @Schema(description = "Optional unit UUID — copy a real id from GET /units")
        UUID unitId,
        @Schema(description = "Optional tax class UUID — copy a real id from GET /tax-classes")
        UUID taxClassId,
        @Schema(description = "Purchase cost per unit", example = "9.50")
        @NotNull @DecimalMin("0.0") BigDecimal costPrice,
        @Schema(description = "Retail selling price per unit", example = "15.00")
        @NotNull @DecimalMin("0.0") BigDecimal sellingPrice,
        @Schema(description = "Wholesale price per unit", example = "12.00")
        BigDecimal wholesalePrice,
        @Schema(description = "Minimum stock alert level", example = "10")
        Integer minStockLevel,
        @Schema(description = "Maximum stock level", example = "200")
        Integer maxStockLevel,
        @Schema(description = "Quantity that triggers a reorder", example = "20")
        Integer reorderPoint,
        @Schema(description = "Quantity to order when reorder point is hit", example = "50")
        Integer reorderQuantity,
        @Schema(description = "Short shelf/catalog description", example = "Dark roast whole bean coffee")
        @Size(max = 500) String shortDescription,
        @Schema(description = "Full description", example = "100% Arabica dark roast whole bean coffee, 1 kg bag.")
        String description,
        @Schema(description = "Main product image URL", example = "https://example.com/images/espresso-beans.jpg")
        String mainImageUrl
) {
}
