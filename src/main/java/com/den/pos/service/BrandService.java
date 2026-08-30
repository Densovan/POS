package com.den.pos.service;

import com.den.pos.web.dto.BrandRequest;
import com.den.pos.web.dto.BrandResponse;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    BrandResponse create(BrandRequest request);

    BrandResponse getById(UUID id);

    List<BrandResponse> findAll();

    List<BrandResponse> findAllActive();

    BrandResponse update(UUID id, BrandRequest request);

    void deactivate(UUID id);
}
