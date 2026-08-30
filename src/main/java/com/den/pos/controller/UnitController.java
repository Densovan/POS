package com.den.pos.controller;

import com.den.pos.service.UnitService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.UnitRequest;
import com.den.pos.web.dto.UnitResponse;
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
@RequestMapping("/units")
@PreAuthorize("hasAuthority('units:read')")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('units:create')")
    public ResponseEntity<ApiResponse<UnitResponse>> create(@Valid @RequestBody UnitRequest request) {
        UnitResponse response = unitService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Unit created successfully"));
    }

    @GetMapping
    public ApiResponse<List<UnitResponse>> findAll() {
        return ApiResponse.success(unitService.findAll(), "Units retrieved successfully");
    }

    @GetMapping("/base")
    public ApiResponse<List<UnitResponse>> findBaseUnits() {
        return ApiResponse.success(unitService.findBaseUnits(), "Base units retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UnitResponse> getById(@PathVariable UUID id) {
        return ApiResponse.success(unitService.getById(id), "Unit retrieved successfully");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('units:update')")
    public ApiResponse<UnitResponse> update(@PathVariable UUID id, @Valid @RequestBody UnitRequest request) {
        return ApiResponse.success(unitService.update(id, request), "Unit updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('units:delete')")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        unitService.delete(id);
        return ApiResponse.successMessage("Unit deleted successfully");
    }
}
