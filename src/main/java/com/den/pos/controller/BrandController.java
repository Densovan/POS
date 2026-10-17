package com.den.pos.controller;

import com.den.pos.service.BrandService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.BrandRequest;
import com.den.pos.web.dto.BrandResponse;
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

@Tag(name = "Brands", description = "Product brands")
@RestController
@RequestMapping("/brands")
@PreAuthorize("hasAuthority('brands:read')")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @Operation(summary = "Create a brand", description = "Slug is generated automatically; requires brands:create")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('brands:create')")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody BrandRequest request) {
        BrandResponse response = brandService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Brand created successfully"));
    }

    @Operation(summary = "List all brands", description = "Includes inactive brands")
    @GetMapping
    public ApiResponse<List<BrandResponse>> findAll() {
        return ApiResponse.success(brandService.findAll(), "Brands retrieved successfully");
    }

    @Operation(summary = "List active brands only")
    @GetMapping("/active")
    public ApiResponse<List<BrandResponse>> findAllActive() {
        return ApiResponse.success(brandService.findAllActive(), "Active brands retrieved successfully");
    }

    @Operation(summary = "Get a brand by id")
    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> getById(
            @Parameter(description = "Brand UUID") @PathVariable UUID id) {
        return ApiResponse.success(brandService.getById(id), "Brand retrieved successfully");
    }

    @Operation(summary = "Update a brand")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('brands:update')")
    public ApiResponse<BrandResponse> update(
            @Parameter(description = "Brand UUID") @PathVariable UUID id,
            @Valid @RequestBody BrandRequest request) {
        return ApiResponse.success(brandService.update(id, request), "Brand updated successfully");
    }

    @Operation(summary = "Deactivate a brand")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('brands:delete')")
    public ApiResponse<Void> deactivate(@PathVariable UUID id) {
        brandService.deactivate(id);
        return ApiResponse.successMessage("Brand deactivated successfully");
    }
}
