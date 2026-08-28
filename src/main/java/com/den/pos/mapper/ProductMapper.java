package com.den.pos.mapper;

import com.den.pos.entity.catalog.Product;
import com.den.pos.web.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(), p.getName(), p.getSku(), p.getBarcode(),
                p.getCategoryId(), p.getBrandId(), p.getUnitId(), p.getTaxClassId(),
                p.getCostPrice(), p.getSellingPrice(), p.getWholesalePrice(), p.getProfitMargin(),
                p.getMinStockLevel(), p.getReorderPoint(), p.getShortDescription(),
                p.getDescription(), p.getMainImageUrl(), p.getIsActive(), p.getCreatedAt());
    }
}
