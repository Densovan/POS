package com.den.pos.mapper;

import com.den.pos.entity.catalog.Category;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category c, Map<UUID, AuditUserRef> userRefs) {
        return new CategoryResponse(
                c.getId(), c.getParentId(), c.getName(), c.getSlug(),
                c.getDescription(), c.getImageUrl(), c.getTaxClassId(),
                c.getIsActive(), c.getDisplayOrder(), c.getCreatedAt(),
                c.getUpdatedAt(), c.getCreatedBy(), c.getUpdatedBy(),
                c.getCreatedBy() == null ? null : userRefs.get(c.getCreatedBy()),
                c.getUpdatedBy() == null ? null : userRefs.get(c.getUpdatedBy()),
                List.of());
    }

    public CategoryResponse toTree(Category c, Map<UUID, List<Category>> childrenByParent, Map<UUID, AuditUserRef> userRefs) {
        List<CategoryResponse> children = childrenByParent
                .getOrDefault(c.getId(), List.of())
                .stream()
                .map(child -> toTree(child, childrenByParent, userRefs))
                .toList();
        return new CategoryResponse(
                c.getId(), c.getParentId(), c.getName(), c.getSlug(),
                c.getDescription(), c.getImageUrl(), c.getTaxClassId(),
                c.getIsActive(), c.getDisplayOrder(), c.getCreatedAt(),
                c.getUpdatedAt(), c.getCreatedBy(), c.getUpdatedBy(),
                c.getCreatedBy() == null ? null : userRefs.get(c.getCreatedBy()),
                c.getUpdatedBy() == null ? null : userRefs.get(c.getUpdatedBy()),
                children);
    }
}
