package com.den.pos.controller;

import com.den.pos.service.ProductVariantService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.VariantRequest;
import com.den.pos.web.dto.VariantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Product variants", description = "Sellable variants (size, color, ...) under a product, with their own SKU/barcode and optional price overrides")
@RestController
@RequestMapping("/products/{productId}/variants")
@PreAuthorize("hasAuthority('products:read')")
public class ProductVariantController {

    private final ProductVariantService variantService;

    public ProductVariantController(ProductVariantService variantService) {
        this.variantService = variantService;
    }

    @Operation(summary = "Create a variant", description = "Requires permission products:create. SKU and barcode must be globally unique across all products and variants.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('products:create')")
    public ResponseEntity<ApiResponse<VariantResponse>> create(
            @Parameter(description = "Product UUID") @PathVariable UUID productId,
            @Valid @RequestBody VariantRequest request) {
        VariantResponse response = variantService.create(productId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Variant created successfully"));
    }

    @Operation(summary = "List variants of a product")
    @GetMapping
    public ApiResponse<List<VariantResponse>> findByProduct(
            @Parameter(description = "Product UUID") @PathVariable UUID productId) {
        return ApiResponse.success(variantService.findByProduct(productId), "Variants retrieved successfully");
    }

    @Operation(summary = "Get a variant by id", description = "Returns effective prices: the variant override when set, otherwise the parent product price")
    @GetMapping("/{id}")
    public ApiResponse<VariantResponse> get(
            @Parameter(description = "Product UUID") @PathVariable UUID productId,
            @Parameter(description = "Variant UUID") @PathVariable UUID id) {
        return ApiResponse.success(variantService.get(productId, id), "Variant retrieved successfully");
    }

    @Operation(summary = "Update a variant", description = "Full replace: omitted optional fields fall back to their defaults")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('products:update')")
    public ApiResponse<VariantResponse> update(
            @Parameter(description = "Product UUID") @PathVariable UUID productId,
            @Parameter(description = "Variant UUID") @PathVariable UUID id,
            @Valid @RequestBody VariantRequest request) {
        return ApiResponse.success(variantService.update(productId, id, request), "Variant updated successfully");
    }

    @Operation(summary = "Deactivate a variant", description = "Soft delete; the variant stops being sellable but historical sales keep their data")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('products:delete')")
    public ApiResponse<Void> deactivate(
            @Parameter(description = "Product UUID") @PathVariable UUID productId,
            @Parameter(description = "Variant UUID") @PathVariable UUID id) {
        variantService.deactivate(productId, id);
        return ApiResponse.successMessage("Variant deactivated successfully");
    }
}
