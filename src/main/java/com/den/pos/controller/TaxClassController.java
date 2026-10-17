package com.den.pos.controller;

import com.den.pos.service.TaxClassService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.TaxClassRequest;
import com.den.pos.web.dto.TaxClassResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

@Tag(name = "Tax classes", description = "Tax classes and rates used for pricing")
@RestController
@RequestMapping("/tax-classes")
@PreAuthorize("hasAuthority('taxes:read')")
public class TaxClassController {

    private final TaxClassService taxClassService;

    public TaxClassController(TaxClassService taxClassService) {
        this.taxClassService = taxClassService;
    }

    @Operation(summary = "Create a tax class", description = "Rate is a percentage: 10.00 for 10%")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('taxes:create')")
    public ResponseEntity<ApiResponse<TaxClassResponse>> create(@Valid @RequestBody TaxClassRequest request) {
        TaxClassResponse response = taxClassService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Tax class created successfully"));
    }

    @Operation(summary = "List all tax classes")
    @GetMapping
    public ApiResponse<List<TaxClassResponse>> findAll() {
        return ApiResponse.success(taxClassService.findAll(), "Tax classes retrieved successfully");
    }

    @Operation(summary = "Get a tax class by id")
    @GetMapping("/{id}")
    public ApiResponse<TaxClassResponse> getById(
            @Parameter(description = "Tax class UUID") @PathVariable UUID id) {
        return ApiResponse.success(taxClassService.getById(id), "Tax class retrieved successfully");
    }

    @Operation(summary = "Update a tax class")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('taxes:update')")
    public ApiResponse<TaxClassResponse> update(
            @Parameter(description = "Tax class UUID") @PathVariable UUID id,
            @Valid @RequestBody TaxClassRequest request) {
        return ApiResponse.success(taxClassService.update(id, request), "Tax class updated successfully");
    }

    @Operation(summary = "Delete a tax class", description = "Hard delete; fails with 409 if products reference it")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('taxes:delete')")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        taxClassService.delete(id);
        return ApiResponse.successMessage("Tax class deleted successfully");
    }
}
