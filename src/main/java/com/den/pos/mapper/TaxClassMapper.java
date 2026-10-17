package com.den.pos.mapper;

import com.den.pos.entity.catalog.TaxClass;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.TaxClassResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class TaxClassMapper {

    public TaxClassResponse toResponse(TaxClass t, Map<UUID, AuditUserRef> userRefs) {
        return new TaxClassResponse(
                t.getId(), t.getName(), t.getRate(), t.getDescription(),
                t.getIsDefault(), t.getCreatedAt(), t.getUpdatedAt(),
                t.getCreatedBy(), t.getUpdatedBy(),
                t.getCreatedBy() == null ? null : userRefs.get(t.getCreatedBy()),
                t.getUpdatedBy() == null ? null : userRefs.get(t.getUpdatedBy()));
    }

    public TaxClassResponse toResponse(TaxClass t) {
        return toResponse(t, Map.of());
    }
}
