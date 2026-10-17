package com.den.pos.mapper;

import com.den.pos.entity.catalog.Product;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product p, Map<UUID, AuditUserRef> userRefs) {
        return new ProductResponse(
                p.getId(), p.getName(), p.getSku(), p.getBarcode(),
                p.getCategoryId(), p.getBrandId(), p.getUnitId(), p.getTaxClassId(),
                p.getCostPrice(), p.getSellingPrice(), p.getWholesalePrice(), p.getProfitMargin(),
                p.getMinStockLevel(), p.getReorderPoint(), p.getShortDescription(),
                p.getDescription(), p.getMainImageUrl(), p.getIsActive(), p.getCreatedAt(),
                p.getUpdatedAt(), p.getCreatedBy(), p.getUpdatedBy(),
                p.getCreatedBy() == null ? null : userRefs.get(p.getCreatedBy()),
                p.getUpdatedBy() == null ? null : userRefs.get(p.getUpdatedBy()));
    }

    public ProductResponse toResponse(Product p) {
        return toResponse(p, Map.of());
    }
}
