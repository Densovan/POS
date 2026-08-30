package com.den.pos.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        UUID parentId,
        String name,
        String slug,
        String description,
        String imageUrl,
        UUID taxClassId,
        Boolean isActive,
        Integer displayOrder,
        LocalDateTime createdAt
) {
}
