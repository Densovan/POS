package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Unit;
import com.den.pos.exception.BusinessException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.UnitMapper;
import com.den.pos.repository.catalog.UnitRepository;
import com.den.pos.service.UnitService;
import com.den.pos.web.dto.UnitRequest;
import com.den.pos.web.dto.UnitResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public UnitServiceImpl(UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Override
    @Transactional
    public UnitResponse create(UnitRequest request) {
        Unit unit = new Unit();
        applyRequest(unit, request);
        return unitMapper.toResponse(unitRepository.save(unit));
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse getById(UUID id) {
        return unitRepository.findById(id)
                .map(unitMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> findAll() {
        return unitRepository.findAll().stream().map(unitMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> findBaseUnits() {
        return unitRepository.findBaseUnits().stream().map(unitMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UnitResponse update(UUID id, UnitRequest request) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));
        applyRequest(unit, request);
        return unitMapper.toResponse(unitRepository.save(unit));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!unitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Unit not found");
        }
        try {
            unitRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new BusinessException(HttpStatus.CONFLICT, "Unit is in use and cannot be deleted");
        }
    }

    private void applyRequest(Unit unit, UnitRequest request) {
        unit.setName(request.name());
        unit.setAbbreviation(request.abbreviation());
        unit.setIsBaseUnit(request.isBaseUnit() != null ? request.isBaseUnit() : false);
        unit.setConversionFactor(request.conversionFactor() != null
                ? request.conversionFactor()
                : java.math.BigDecimal.ONE);
        if (Boolean.TRUE.equals(request.isBaseUnit())) {
            unit.setBaseUnitId(null);
        } else {
            unit.setBaseUnitId(request.baseUnitId());
        }
    }
}
