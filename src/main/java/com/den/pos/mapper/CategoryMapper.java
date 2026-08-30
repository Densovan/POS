package com.den.pos.mapper;

import com.den.pos.entity.catalog.Category;
import com.den.pos.web.dto.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category c) {
        return CategoryResponse.of(
                c.getId(), c.getParentId(), c.getName(), c.getSlug(),
                c.getDescription(), c.getImageUrl(), c.getTaxClassId(),
                c.getIsActive(), c.getDisplayOrder(), c.getCreatedAt());
    }

    public CategoryResponse toTree(Category c, Map<UUID, List<Category>> childrenByParent) {
        List<CategoryResponse> children = childrenByParent
                .getOrDefault(c.getId(), List.of())
                .stream()
                .map(child -> toTree(child, childrenByParent))
                .toList();
        return new CategoryResponse(
                c.getId(), c.getParentId(), c.getName(), c.getSlug(),
                c.getDescription(), c.getImageUrl(), c.getTaxClassId(),
                c.getIsActive(), c.getDisplayOrder(), c.getCreatedAt(), children);
    }
}
