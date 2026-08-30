package com.den.pos.controller;

import com.den.pos.service.BrandService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.BrandRequest;
import com.den.pos.web.dto.BrandResponse;
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

@RestController
@RequestMapping("/brands")
@PreAuthorize("hasAuthority('brands:read')")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('brands:create')")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody BrandRequest request) {
        BrandResponse response = brandService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Brand created successfully"));
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> findAll() {
        return ApiResponse.success(brandService.findAll(), "Brands retrieved successfully");
    }

    @GetMapping("/active")
    public ApiResponse<List<BrandResponse>> findAllActive() {
        return ApiResponse.success(brandService.findAllActive(), "Active brands retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> getById(@PathVariable UUID id) {
        return ApiResponse.success(brandService.getById(id), "Brand retrieved successfully");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('brands:update')")
    public ApiResponse<BrandResponse> update(@PathVariable UUID id, @Valid @RequestBody BrandRequest request) {
        return ApiResponse.success(brandService.update(id, request), "Brand updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('brands:delete')")
    public ApiResponse<Void> deactivate(@PathVariable UUID id) {
        brandService.deactivate(id);
        return ApiResponse.successMessage("Brand deactivated successfully");
    }
}
