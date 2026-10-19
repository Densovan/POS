package com.den.pos.service;

import com.den.pos.web.dto.VariantRequest;
import com.den.pos.web.dto.VariantResponse;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {

    VariantResponse create(UUID productId, VariantRequest request);

    List<VariantResponse> findByProduct(UUID productId);

    VariantResponse get(UUID productId, UUID variantId);

    VariantResponse update(UUID productId, UUID variantId, VariantRequest request);

    void deactivate(UUID productId, UUID variantId);
}
