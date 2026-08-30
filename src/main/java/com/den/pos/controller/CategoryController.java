package com.den.pos.controller;

import com.den.pos.service.CategoryService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.CategoryRequest;
import com.den.pos.web.dto.CategoryResponse;
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
@RequestMapping("/categories")
@PreAuthorize("hasAuthority('categories:read')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('categories:create')")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Category created successfully"));
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAll() {
        return ApiResponse.success(categoryService.findAll(), "Categories retrieved successfully");
    }

    @GetMapping("/roots")
    public ApiResponse<List<CategoryResponse>> findRoots() {
        return ApiResponse.success(categoryService.findRoots(), "Root categories retrieved successfully");
    }

    @GetMapping("/by-parent/{parentId}")
    public ApiResponse<List<CategoryResponse>> findByParent(@PathVariable UUID parentId) {
        return ApiResponse.success(categoryService.findByParent(parentId), "Categories retrieved successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable UUID id) {
        return ApiResponse.success(categoryService.getById(id), "Category retrieved successfully");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('categories:update')")
    public ApiResponse<CategoryResponse> update(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.success(categoryService.update(id, request), "Category updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('categories:delete')")
    public ApiResponse<Void> deactivate(@PathVariable UUID id) {
        categoryService.deactivate(id);
        return ApiResponse.successMessage("Category deactivated successfully");
    }
}
