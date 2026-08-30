package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Brand;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.BrandMapper;
import com.den.pos.repository.catalog.BrandRepository;
import com.den.pos.service.BrandService;
import com.den.pos.web.dto.BrandRequest;
import com.den.pos.web.dto.BrandResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    @Transactional
    public BrandResponse create(BrandRequest request) {
        Brand brand = new Brand();
        applyRequest(brand, request);
        brand.setSlug(uniqueSlug(request.name()));
        brand.setIsActive(request.isActive() != null ? request.isActive() : true);
        return brandMapper.toResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(UUID id) {
        return brandRepository.findById(id)
                .map(brandMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> findAll() {
        return brandRepository.findAll().stream().map(brandMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> findAllActive() {
        return brandRepository.findAllActive().stream().map(brandMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public BrandResponse update(UUID id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        applyRequest(brand, request);
        return brandMapper.toResponse(brandRepository.save(brand));
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        brand.setIsActive(false);
        brandRepository.save(brand);
    }

    private void applyRequest(Brand brand, BrandRequest request) {
        brand.setName(request.name());
        brand.setLogoUrl(request.logoUrl());
        brand.setWebsiteUrl(request.websiteUrl());
    }

    private String uniqueSlug(String name) {
        String base = CategoryServiceImpl.slugify(name);
        String candidate = base;
        int suffix = 2;
        while (brandRepository.findBySlug(candidate).isPresent()) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }
}
