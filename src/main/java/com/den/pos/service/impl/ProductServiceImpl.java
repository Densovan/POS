package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Product;
import com.den.pos.exception.ConflictException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.ProductMapper;
import com.den.pos.repository.catalog.ProductRepository;
import com.den.pos.service.ProductService;
import com.den.pos.service.UserReferenceService;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.ProductRequest;
import com.den.pos.web.dto.ProductResponse;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserReferenceService userReferenceService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              UserReferenceService userReferenceService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userReferenceService = userReferenceService;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        Product saved = save(product);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(saved.getCreatedBy(), saved.getUpdatedBy());
        return productMapper.toResponse(saved, userRefs);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(product.getCreatedBy(), product.getUpdatedBy());
        return productMapper.toResponse(product, userRefs);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.searchByKeyword(keyword, pageable);
        List<UUID> userIds = products.getContent().stream()
                .flatMap(p -> java.util.stream.Stream.of(p.getCreatedBy(), p.getUpdatedBy()))
                .filter(java.util.Objects::nonNull)
                .toList();
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(userIds);
        return products.map(p -> productMapper.toResponse(p, userRefs));
    }

    @Override
    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        applyRequest(product, request);
        Product saved = save(product);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(saved.getCreatedBy(), saved.getUpdatedBy());
        return productMapper.toResponse(saved, userRefs);
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    private Product save(Product product) {
        try {
            return productRepository.saveAndFlush(product);
        } catch (DataIntegrityViolationException ex) {
            throw toConflict(ex, product);
        }
    }

    private ConflictException toConflict(DataIntegrityViolationException ex, Product product) {
        SQLException cause = findSqlException(ex);
        String state = cause != null ? cause.getSQLState() : null;
        if ("23505".equals(state)) {
            return new ConflictException("SKU already exists: " + product.getSku());
        }
        if ("23503".equals(state)) {
            return new ConflictException("Referenced record does not exist (invalid category, brand, unit, or tax class)");
        }
        return new ConflictException("Data integrity violation");
    }

    private SQLException findSqlException(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof SQLException sql) {
                return sql;
            }
            current = current.getCause();
        }
        return null;
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setSku(request.sku());
        product.setBarcode(request.barcode());
        product.setCategoryId(request.categoryId());
        product.setBrandId(request.brandId());
        product.setUnitId(request.unitId());
        product.setTaxClassId(request.taxClassId());
        product.setCostPrice(request.costPrice());
        product.setSellingPrice(request.sellingPrice());
        product.setWholesalePrice(request.wholesalePrice());
        product.setMinStockLevel(request.minStockLevel() != null ? request.minStockLevel() : 0);
        product.setMaxStockLevel(request.maxStockLevel());
        product.setReorderPoint(request.reorderPoint() != null ? request.reorderPoint() : 0);
        product.setReorderQuantity(request.reorderQuantity());
        product.setShortDescription(request.shortDescription());
        product.setDescription(request.description());
        product.setMainImageUrl(request.mainImageUrl());
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        if (product.getIsVariantParent() == null) {
            product.setIsVariantParent(false);
        }
        if (product.getIsTrackStock() == null) {
            product.setIsTrackStock(true);
        }
        if (product.getIsSerialized() == null) {
            product.setIsSerialized(false);
        }
        if (product.getHasBatches() == null) {
            product.setHasBatches(false);
        }
    }
}
