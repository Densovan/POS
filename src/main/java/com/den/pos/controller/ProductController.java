package com.den.pos.controller;

import com.den.pos.service.ProductService;
import com.den.pos.web.dto.ApiResponse;
import com.den.pos.web.dto.ProductRequest;
import com.den.pos.web.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Products", description = "Product catalog: create, read, search, update and deactivate")
@RestController
@RequestMapping("/products")
@PreAuthorize("hasAuthority('products:read')")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a product", description = "Requires permission products:create")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('products:create')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    @Operation(summary = "Get a product by id")
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(
            @Parameter(description = "Product UUID") @PathVariable UUID id) {
        return ApiResponse.success(productService.getById(id), "Product retrieved successfully");
    }

    @Operation(summary = "Search products", description = "Case-insensitive match on name, SKU or barcode")
    @GetMapping("/search")
    public ApiResponse<Page<ProductResponse>> search(
            @Parameter(description = "Search keyword", example = "coffee")
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 20, sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(productService.search(keyword, pageable), "Products retrieved successfully");
    }

    @Operation(summary = "Update a product")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('products:update')")
    public ApiResponse<ProductResponse> update(
            @Parameter(description = "Product UUID") @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        return ApiResponse.success(productService.update(id, request), "Product updated successfully");
    }

    @Operation(summary = "Deactivate a product", description = "Soft delete; historical sales keep their data")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('products:delete')")
    public ApiResponse<Void> deactivate(@PathVariable UUID id) {
        productService.deactivate(id);
        return ApiResponse.successMessage("Product deactivated successfully");
    }
}
