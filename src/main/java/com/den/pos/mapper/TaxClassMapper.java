package com.den.pos.mapper;

import com.den.pos.entity.catalog.TaxClass;
import com.den.pos.web.dto.TaxClassResponse;
import org.springframework.stereotype.Component;

@Component
public class TaxClassMapper {

    public TaxClassResponse toResponse(TaxClass t) {
        return new TaxClassResponse(
                t.getId(), t.getName(), t.getRate(), t.getDescription(),
                t.getIsDefault(), t.getCreatedAt(), t.getUpdatedAt());
    }
}
