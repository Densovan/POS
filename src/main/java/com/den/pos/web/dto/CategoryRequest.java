package com.den.pos.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequest(
        UUID parentId,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 10000) String description,
        String imageUrl,
        UUID taxClassId,
        Boolean isActive,
        Integer displayOrder
) {
}
