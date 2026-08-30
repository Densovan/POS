package com.den.pos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TaxClassRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull BigDecimal rate,
        @Size(max = 10000) String description,
        Boolean isDefault
) {
}
