package com.den.pos.mapper;

import com.den.pos.entity.catalog.Brand;
import com.den.pos.web.dto.BrandResponse;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand b) {
        return new BrandResponse(
                b.getId(), b.getName(), b.getSlug(), b.getLogoUrl(),
                b.getWebsiteUrl(), b.getIsActive(), b.getCreatedAt());
    }
}
