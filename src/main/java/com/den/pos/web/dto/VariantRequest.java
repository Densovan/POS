package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Map;

@Schema(name = "VariantRequest", description = "Product variant create/update payload. SKU and barcode must be unique across all products AND variants (a barcode scan must resolve to exactly one sellable item)")
public record VariantRequest(
        @Schema(description = "Variant SKU (globally unique)", example = "TEE-CLSC-S-BLK")
        @NotBlank
        @Size(max = 100)
        String sku,
        @Schema(description = "Variant barcode (globally unique when set)", example = "8851001000022")
        @Size(max = 100)
        String barcode,
        @Schema(description = "Free-form variant attributes, e.g. {\"color\":\"Black\",\"size\":\"S\"}", example = "{\"color\": \"Black\", \"size\": \"S\"}")
        Map<String, Object> attributes,
        @Schema(description = "Optional cost override; falls back to the parent product's price", example = "8.00")
        @DecimalMin("0.0")
        BigDecimal costPrice,
        @Schema(description = "Optional selling price override; falls back to the parent product's price", example = "19.00")
        @DecimalMin("0.0")
        BigDecimal sellingPrice,
        @Schema(description = "Minimum stock alert level for this variant", example = "5")
        Integer minStockLevel,
        @Schema(description = "Quantity that triggers a reorder", example = "10")
        Integer reorderPoint,
        @Schema(description = "Variant image URL", example = "https://example.com/images/tee-black-s.jpg")
        String imageUrl,
        @Schema(description = "Whether the variant is sellable", example = "true")
        Boolean isActive,
        @Schema(description = "Track stock per warehouse for this variant", example = "true")
        Boolean isTrackStock,
        @Schema(description = "Track serial numbers for this variant", example = "false")
        Boolean isSerialized,
        @Schema(description = "Track batches/expiry for this variant", example = "false")
        Boolean hasBatches
) {
}
