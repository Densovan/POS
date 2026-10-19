package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Product;
import com.den.pos.entity.catalog.ProductVariant;
import com.den.pos.exception.ConflictException;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.ProductVariantMapper;
import com.den.pos.repository.catalog.ProductRepository;
import com.den.pos.repository.catalog.ProductVariantRepository;
import com.den.pos.service.ProductVariantService;
import com.den.pos.service.UserReferenceService;
import com.den.pos.web.dto.AuditUserRef;
import com.den.pos.web.dto.VariantRequest;
import com.den.pos.web.dto.VariantResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductVariantMapper variantMapper;
    private final UserReferenceService userReferenceService;

    public ProductVariantServiceImpl(ProductRepository productRepository,
                                     ProductVariantRepository variantRepository,
                                     ProductVariantMapper variantMapper,
                                     UserReferenceService userReferenceService) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.variantMapper = variantMapper;
        this.userReferenceService = userReferenceService;
    }

    @Override
    @Transactional
    public VariantResponse create(UUID productId, VariantRequest request) {
        Product product = requireProduct(productId);
        ensureSkuAvailable(request.sku(), null);
        ensureBarcodeAvailable(request.barcode(), null);

        ProductVariant variant = new ProductVariant();
        variant.setProductId(productId);
        applyRequest(variant, request);
        ProductVariant saved = save(variant);

        if (!Boolean.TRUE.equals(product.getIsVariantParent())) {
            product.setIsVariantParent(true);
            productRepository.save(product);
        }

        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(saved.getCreatedBy(), saved.getUpdatedBy());
        return variantMapper.toResponse(saved, product, userRefs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantResponse> findByProduct(UUID productId) {
        Product product = requireProduct(productId);
        List<ProductVariant> variants = variantRepository.findByProductId(productId);
        List<UUID> userIds = variants.stream()
                .flatMap(v -> Stream.of(v.getCreatedBy(), v.getUpdatedBy()))
                .filter(Objects::nonNull)
                .toList();
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(userIds);
        return variants.stream()
                .map(v -> variantMapper.toResponse(v, product, userRefs))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VariantResponse get(UUID productId, UUID variantId) {
        Product product = requireProduct(productId);
        ProductVariant variant = requireVariant(productId, variantId);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(variant.getCreatedBy(), variant.getUpdatedBy());
        return variantMapper.toResponse(variant, product, userRefs);
    }

    @Override
    @Transactional
    public VariantResponse update(UUID productId, UUID variantId, VariantRequest request) {
        Product product = requireProduct(productId);
        ProductVariant variant = requireVariant(productId, variantId);
        ensureSkuAvailable(request.sku(), variant.getId());
        ensureBarcodeAvailable(request.barcode(), variant.getId());

        applyRequest(variant, request);
        ProductVariant saved = save(variant);
        Map<UUID, AuditUserRef> userRefs = userReferenceService.resolveReferences(saved.getCreatedBy(), saved.getUpdatedBy());
        return variantMapper.toResponse(saved, product, userRefs);
    }

    @Override
    @Transactional
    public void deactivate(UUID productId, UUID variantId) {
        requireProduct(productId);
        ProductVariant variant = requireVariant(productId, variantId);
        variant.setIsActive(false);
        variantRepository.save(variant);
    }

    private Product requireProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private ProductVariant requireVariant(UUID productId, UUID variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
        if (!variant.getProductId().equals(productId)) {
            throw new ResourceNotFoundException("Product variant not found");
        }
        return variant;
    }

    private void ensureSkuAvailable(String sku, UUID selfId) {
        variantRepository.findBySku(sku)
                .filter(existing -> selfId == null || !existing.getId().equals(selfId))
                .ifPresent(existing -> {
                    throw new ConflictException("SKU already exists: " + sku);
                });
        if (productRepository.findBySku(sku).isPresent()) {
            throw new ConflictException("SKU already exists: " + sku);
        }
    }

    private void ensureBarcodeAvailable(String barcode, UUID selfId) {
        if (barcode == null || barcode.isBlank()) {
            return;
        }
        variantRepository.findByBarcode(barcode)
                .filter(existing -> selfId == null || !existing.getId().equals(selfId))
                .ifPresent(existing -> {
                    throw new ConflictException("Barcode already exists: " + barcode);
                });
        if (productRepository.findByBarcode(barcode).isPresent()) {
            throw new ConflictException("Barcode already exists: " + barcode);
        }
    }

    private ProductVariant save(ProductVariant variant) {
        try {
            return variantRepository.saveAndFlush(variant);
        } catch (DataIntegrityViolationException ex) {
            throw toConflict(ex, variant);
        }
    }

    private ConflictException toConflict(DataIntegrityViolationException ex, ProductVariant variant) {
        SQLException cause = findSqlException(ex);
        String state = cause != null ? cause.getSQLState() : null;
        if ("23505".equals(state)) {
            return new ConflictException("SKU or barcode already exists: " + variant.getSku());
        }
        if ("23503".equals(state)) {
            return new ConflictException("Referenced record does not exist (invalid product)");
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

    private void applyRequest(ProductVariant variant, VariantRequest request) {
        variant.setSku(request.sku());
        variant.setBarcode(request.barcode());
        variant.setAttributes(variantMapper.toAttributesJson(request.attributes()));
        variant.setCostPrice(request.costPrice());
        variant.setSellingPrice(request.sellingPrice());
        variant.setMinStockLevel(request.minStockLevel() != null ? request.minStockLevel() : 0);
        variant.setReorderPoint(request.reorderPoint() != null ? request.reorderPoint() : 0);
        variant.setImageUrl(request.imageUrl());
        variant.setIsActive(request.isActive());
        variant.setIsTrackStock(request.isTrackStock());
        variant.setIsSerialized(request.isSerialized());
        variant.setHasBatches(request.hasBatches());
        if (variant.getIsActive() == null) {
            variant.setIsActive(true);
        }
        if (variant.getIsTrackStock() == null) {
            variant.setIsTrackStock(true);
        }
        if (variant.getIsSerialized() == null) {
            variant.setIsSerialized(false);
        }
        if (variant.getHasBatches() == null) {
            variant.setHasBatches(false);
        }
    }
}
