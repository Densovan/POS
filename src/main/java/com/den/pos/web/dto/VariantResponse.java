package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Schema(name = "VariantResponse", description = "Product variant with its own SKU, attributes, optional price overrides and flags")
public record VariantResponse(
        @Schema(description = "Variant UUID")
        UUID id,
        @Schema(description = "Parent product UUID")
        UUID productId,
        @Schema(description = "Variant SKU", example = "TEE-CLSC-S-BLK")
        String sku,
        @Schema(description = "Variant barcode", example = "8851001000022")
        String barcode,
        @Schema(description = "Free-form attributes", example = "{\"color\": \"Black\", \"size\": \"S\"}")
        Map<String, Object> attributes,
        @Schema(description = "Variant cost override, null to use the product's price", example = "8.00")
        BigDecimal costPrice,
        @Schema(description = "Variant selling price override, null to use the product's price", example = "19.00")
        BigDecimal sellingPrice,
        @Schema(description = "Cost actually used at checkout: variant override or the product's cost", example = "8.00")
        BigDecimal effectiveCostPrice,
        @Schema(description = "Price actually charged at checkout: variant override or the product's price", example = "19.00")
        BigDecimal effectiveSellingPrice,
        @Schema(description = "Minimum stock alert level", example = "5")
        Integer minStockLevel,
        @Schema(description = "Reorder point", example = "10")
        Integer reorderPoint,
        @Schema(description = "Variant image URL")
        String imageUrl,
        @Schema(description = "Whether the variant is sellable", example = "true")
        Boolean isActive,
        @Schema(description = "Whether stock is tracked for this variant", example = "true")
        Boolean isTrackStock,
        @Schema(description = "Whether serial numbers are tracked", example = "false")
        Boolean isSerialized,
        @Schema(description = "Whether batches/expiry are tracked", example = "false")
        Boolean hasBatches,
        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,
        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt,
        @Schema(description = "Created by user UUID")
        UUID createdBy,
        @Schema(description = "Last updated by user UUID")
        UUID updatedBy,
        @Schema(description = "Display info of the creating user")
        AuditUserRef createdByUser,
        @Schema(description = "Display info of the last updating user")
        AuditUserRef updatedByUser
) {
}
