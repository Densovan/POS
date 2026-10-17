package com.den.pos.web.dto;

import java.time.LocalDateTime;
import java.util.List;
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID createdBy,
        UUID updatedBy,
        AuditUserRef createdByUser,
        AuditUserRef updatedByUser,
        List<CategoryResponse> children
) {

    public CategoryResponse {
        children = children == null ? List.of() : children;
    }

    public static CategoryResponse of(UUID id, UUID parentId, String name, String slug,
                                      String description, String imageUrl, UUID taxClassId,
                                      Boolean isActive, Integer displayOrder, LocalDateTime createdAt,
                                      LocalDateTime updatedAt, UUID createdBy, UUID updatedBy) {
        return new CategoryResponse(id, parentId, name, slug, description, imageUrl,
                taxClassId, isActive, displayOrder, createdAt, updatedAt, createdBy, updatedBy,
                null, null, List.of());
    }
}
