package com.den.pos.mapper;

import com.den.pos.entity.catalog.Brand;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.BrandResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand b, Map<UUID, AuditUserRef> userRefs) {
        return new BrandResponse(
                b.getId(), b.getName(), b.getSlug(), b.getLogoUrl(),
                b.getWebsiteUrl(), b.getIsActive(), b.getCreatedAt(),
                b.getUpdatedAt(), b.getCreatedBy(), b.getUpdatedBy(),
                b.getCreatedBy() == null ? null : userRefs.get(b.getCreatedBy()),
                b.getUpdatedBy() == null ? null : userRefs.get(b.getUpdatedBy()));
    }

    public BrandResponse toResponse(Brand b) {
        return toResponse(b, Map.of());
    }
}
