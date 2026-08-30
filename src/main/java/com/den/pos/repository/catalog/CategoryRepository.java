package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(value = "SELECT * FROM categories WHERE slug = :slug AND is_deleted = false", nativeQuery = true)
    Optional<Category> findBySlug(@Param("slug") String slug);

    @Query(value = "SELECT * FROM categories WHERE parent_id = :parentId AND is_deleted = false ORDER BY display_order", nativeQuery = true)
    List<Category> findByParentId(@Param("parentId") UUID parentId);

    @Query(value = "SELECT * FROM categories WHERE parent_id IS NULL AND is_active = true AND is_deleted = false ORDER BY display_order", nativeQuery = true)
    List<Category> findRootCategories();
}
