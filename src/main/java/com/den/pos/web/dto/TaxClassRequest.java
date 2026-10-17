package com.den.pos.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(name = "TaxClassRequest", description = "Tax class create/update payload (rate is a percentage, e.g. 10.00 = 10%)")
public record TaxClassRequest(
        @Schema(description = "Tax class name", example = "VAT Standard (10%)")
        @NotBlank @Size(max = 100) String name,
        @Schema(description = "Tax rate as a percentage", example = "10.00")
        @NotNull BigDecimal rate,
        @Schema(description = "Description / legal note", example = "Standard VAT applied to most goods")
        @Size(max = 10000) String description,
        @Schema(description = "Use as the shop default tax class", example = "false")
        Boolean isDefault
) {
}
