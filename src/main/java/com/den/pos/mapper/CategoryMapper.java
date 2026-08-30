package com.den.pos.mapper;

import com.den.pos.entity.catalog.Category;
import com.den.pos.web.dto.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.getId(), c.getParentId(), c.getName(), c.getSlug(),
                c.getDescription(), c.getImageUrl(), c.getTaxClassId(),
                c.getIsActive(), c.getDisplayOrder(), c.getCreatedAt());
    }
}
