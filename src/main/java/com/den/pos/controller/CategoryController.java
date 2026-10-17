package com.den.pos.controller;

import com.den.pos.service.CategoryService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.CategoryRequest;
import com.den.pos.web.dto.CategoryResponse;
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

@Tag(name = "Categories", description = "Hierarchical product categories")
@RestController
@RequestMapping("/categories")
@PreAuthorize("hasAuthority('categories:read')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a category", description = "Slug is generated automatically; requires categories:create")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('categories:create')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Category created successfully"));
    }

    @Operation(summary = "List all categories as a tree")
    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAll() {
        return ApiResponse.success(categoryService.findAll(), "Categories retrieved successfully");
    }

    @Operation(summary = "List root categories")
    @GetMapping("/roots")
    public ApiResponse<List<CategoryResponse>> findRoots() {
        return ApiResponse.success(categoryService.findRoots(), "Root categories retrieved successfully");
    }

    @Operation(summary = "List direct children of a category")
    @GetMapping("/by-parent/{parentId}")
    public ApiResponse<List<CategoryResponse>> findByParent(
            @Parameter(description = "Parent category UUID") @PathVariable UUID parentId) {
        return ApiResponse.success(categoryService.findByParent(parentId), "Categories retrieved successfully");
    }

    @Operation(summary = "Get a category by id")
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(
            @Parameter(description = "Category UUID") @PathVariable UUID id) {
        return ApiResponse.success(categoryService.getById(id), "Category retrieved successfully");
    }

    @Operation(summary = "Update a category")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('categories:update')")
    public ApiResponse<CategoryResponse> update(
            @Parameter(description = "Category UUID") @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success(categoryService.update(id, request), "Category updated successfully");
    }

    @Operation(summary = "Deactivate a category")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('categories:delete')")
    public ApiResponse<Void> deactivate(@PathVariable UUID id) {
        categoryService.deactivate(id);
        return ApiResponse.successMessage("Category deactivated successfully");
    }
}
