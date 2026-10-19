package com.den.pos.mapper;

import com.den.pos.entity.catalog.Product;
import com.den.pos.entity.catalog.ProductVariant;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.VariantResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
public class ProductVariantMapper {

    public VariantResponse toResponse(ProductVariant v, Product product, Map<UUID, AuditUserRef> userRefs) {
        return new VariantResponse(
                v.getId(), v.getProductId(), v.getSku(), v.getBarcode(),
                toAttributes(v.getAttributes()),
                v.getCostPrice(), v.getSellingPrice(),
                effective(v.getCostPrice(), product.getCostPrice()),
                effective(v.getSellingPrice(), product.getSellingPrice()),
                v.getMinStockLevel(), v.getReorderPoint(), v.getImageUrl(),
                v.getIsActive(), v.getIsTrackStock(), v.getIsSerialized(), v.getHasBatches(),
                v.getCreatedAt(), v.getUpdatedAt(), v.getCreatedBy(), v.getUpdatedBy(),
                v.getCreatedBy() == null ? null : userRefs.get(v.getCreatedBy()),
                v.getUpdatedBy() == null ? null : userRefs.get(v.getUpdatedBy()));
    }

    private BigDecimal effective(BigDecimal override, BigDecimal parent) {
        return override != null ? override : parent;
    }

    public String toAttributesJson(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return "{}";
        }
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(attributes);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid attributes payload", e);
        }
    }

    private Map<String, Object> toAttributes(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                    });
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return Map.of();
        }
    }
}
