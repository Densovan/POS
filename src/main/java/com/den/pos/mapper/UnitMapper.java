package com.den.pos.mapper;

import com.den.pos.entity.catalog.Unit;
import com.den.pos.web.dto.UnitResponse;
import org.springframework.stereotype.Component;

@Component
public class UnitMapper {

    public UnitResponse toResponse(Unit u) {
        return new UnitResponse(
                u.getId(), u.getName(), u.getAbbreviation(), u.getIsBaseUnit(),
                u.getConversionFactor(), u.getBaseUnitId(), u.getCreatedAt());
    }
}
