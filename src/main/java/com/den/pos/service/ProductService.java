package com.den.pos.service;

import com.den.pos.web.dto.ProductRequest;
import com.den.pos.web.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse getById(UUID id);

    Page<ProductResponse> search(String keyword, Pageable pageable);

    ProductResponse update(UUID id, ProductRequest request);

    void deactivate(UUID id);
}
