package com.den.pos.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BrandResponse(
        UUID id,
        String name,
        String slug,
        String logoUrl,
        String websiteUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID createdBy,
        UUID updatedBy,
        AuditUserRef createdByUser,
        AuditUserRef updatedByUser
) {
}
