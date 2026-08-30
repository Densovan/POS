package com.den.pos.service;

import com.den.pos.web.dto.UnitRequest;
import com.den.pos.web.dto.UnitResponse;

import java.util.List;
import java.util.UUID;

public interface UnitService {

    UnitResponse create(UnitRequest request);

    UnitResponse getById(UUID id);

    List<UnitResponse> findAll();

    List<UnitResponse> findBaseUnits();

    UnitResponse update(UUID id, UnitRequest request);

    void delete(UUID id);
}
