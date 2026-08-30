package com.den.pos.service.impl;

import com.den.pos.entity.catalog.TaxClass;
import com.den.pos.exception.BusinessException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.TaxClassMapper;
import com.den.pos.repository.catalog.TaxClassRepository;
import com.den.pos.service.TaxClassService;
import com.den.pos.web.dto.TaxClassRequest;
import com.den.pos.web.dto.TaxClassResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaxClassServiceImpl implements TaxClassService {

    private final TaxClassRepository taxClassRepository;
    private final TaxClassMapper taxClassMapper;

    public TaxClassServiceImpl(TaxClassRepository taxClassRepository, TaxClassMapper taxClassMapper) {
        this.taxClassRepository = taxClassRepository;
        this.taxClassMapper = taxClassMapper;
    }

    @Override
    @Transactional
    public TaxClassResponse create(TaxClassRequest request) {
        TaxClass taxClass = new TaxClass();
        applyRequest(taxClass, request);
        TaxClass saved = taxClassRepository.save(taxClass);
        if (Boolean.TRUE.equals(request.isDefault())) {
            taxClassRepository.setDefault(saved.getId());
            saved.setIsDefault(true);
        }
        return taxClassMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TaxClassResponse getById(UUID id) {
        return taxClassRepository.findById(id)
                .map(taxClassMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tax class not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxClassResponse> findAll() {
        return taxClassRepository.findAllOrderByRate().stream().map(taxClassMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public TaxClassResponse update(UUID id, TaxClassRequest request) {
        TaxClass taxClass = taxClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax class not found"));
        applyRequest(taxClass, request);
        TaxClass saved = taxClassRepository.save(taxClass);
        if (Boolean.TRUE.equals(request.isDefault())) {
            taxClassRepository.setDefault(saved.getId());
            saved.setIsDefault(true);
        }
        return taxClassMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!taxClassRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tax class not found");
        }
        try {
            taxClassRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new BusinessException(HttpStatus.CONFLICT, "Tax class is in use and cannot be deleted");
        }
    }

    private void applyRequest(TaxClass taxClass, TaxClassRequest request) {
        taxClass.setName(request.name());
        taxClass.setRate(request.rate());
        taxClass.setDescription(request.description());
        taxClass.setIsDefault(request.isDefault() != null ? request.isDefault() : false);
    }
}
