package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Brand;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.BrandMapper;
import com.den.pos.repository.catalog.BrandRepository;
import com.den.pos.service.BrandService;
import com.den.pos.service.UserReferenceService;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.BrandRequest;
import com.den.pos.web.dto.BrandResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final UserReferenceService userReferenceService;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper,
                            UserReferenceService userReferenceService) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.userReferenceService = userReferenceService;
    }

    @Override
    @Transactional
    public BrandResponse create(BrandRequest request) {
        Brand brand = new Brand();
        applyRequest(brand, request);
        brand.setSlug(uniqueSlug(request.name()));
        brand.setIsActive(request.isActive() != null ? request.isActive() : true);
        Brand saved = brandRepository.saveAndFlush(brand);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(
                java.util.stream.Stream.of(saved.getCreatedBy(), saved.getUpdatedBy())
                        .filter(java.util.Objects::nonNull)
                        .toList());
        return brandMapper.toResponse(saved, userRefs);
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(
                java.util.stream.Stream.of(brand.getCreatedBy(), brand.getUpdatedBy())
                        .filter(java.util.Objects::nonNull)
                        .toList());
        return brandMapper.toResponse(brand, userRefs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> findAll() {
        return toResponses(brandRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> findAllActive() {
        return toResponses(brandRepository.findAllActive());
    }

    @Override
    @Transactional
    public BrandResponse update(UUID id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        applyRequest(brand, request);
        Brand saved = brandRepository.saveAndFlush(brand);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(
                java.util.stream.Stream.of(saved.getCreatedBy(), saved.getUpdatedBy())
                        .filter(java.util.Objects::nonNull)
                        .toList());
        return brandMapper.toResponse(saved, userRefs);
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

    private List<BrandResponse> toResponses(Collection<Brand> brands) {
        List<UUID> userIds = brands.stream()
                .flatMap(b -> java.util.stream.Stream.of(b.getCreatedBy(), b.getUpdatedBy()))
                .filter(java.util.Objects::nonNull)
                .toList();
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(userIds);
        return brands.stream().map(b -> brandMapper.toResponse(b, userRefs)).toList();
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
