package com.den.pos.service.impl;

import com.den.pos.entity.catalog.Category;
import com.den.pos.exception.ResourceNotFoundException;
import com.den.pos.mapper.CategoryMapper;
import com.den.pos.repository.catalog.CategoryRepository;
import com.den.pos.service.CategoryService;
import com.den.pos.web.dto.CategoryRequest;
import com.den.pos.web.dto.CategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Category category = new Category();
        applyRequest(category, request);
        category.setSlug(uniqueSlug(request.name()));
        category.setIsActive(request.isActive() != null ? request.isActive() : true);
        category.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : 0);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.toTree(category, childrenByParent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return findAllRoots();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findRoots() {
        return findAllRoots();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findByParent(UUID parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(c -> categoryMapper.toTree(c, childrenByParent()))
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        applyRequest(category, request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setIsActive(false);
        categoryRepository.save(category);
    }

    private void applyRequest(Category category, CategoryRequest request) {
        category.setParentId(request.parentId());
        category.setName(request.name());
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());
        category.setTaxClassId(request.taxClassId());
    }

    private List<CategoryResponse> findAllRoots() {
        return categoryRepository.findAllActive().stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList())
                .stream()
                .map(c -> categoryMapper.toTree(c, childrenByParent()))
                .toList();
    }

    private Map<UUID, List<Category>> childrenByParent() {
        return categoryRepository.findAllActive().stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));
    }

    private String uniqueSlug(String name) {
        String base = slugify(name);
        String candidate = base;
        int suffix = 2;
        while (categoryRepository.findBySlug(candidate).isPresent()) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }

    static String slugify(String value) {
        return value.toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s_]+", "-")
                .replaceAll("-+", "-");
    }
}
