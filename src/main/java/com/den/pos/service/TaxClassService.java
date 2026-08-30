package com.den.pos.service;

import com.den.pos.web.dto.TaxClassRequest;
import com.den.pos.web.dto.TaxClassResponse;

import java.util.List;
import java.util.UUID;

public interface TaxClassService {

    TaxClassResponse create(TaxClassRequest request);

    TaxClassResponse getById(UUID id);

    List<TaxClassResponse> findAll();

    TaxClassResponse update(UUID id, TaxClassRequest request);

    void delete(UUID id);
}
