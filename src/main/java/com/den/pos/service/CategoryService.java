package com.den.pos.service;

import com.den.pos.web.dto.CategoryRequest;
import com.den.pos.web.dto.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse getById(UUID id);

    List<CategoryResponse> findAll();

    List<CategoryResponse> findRoots();

    List<CategoryResponse> findByParent(UUID parentId);

    CategoryResponse update(UUID id, CategoryRequest request);

    void deactivate(UUID id);
}
