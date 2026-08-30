package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    @Query(value = "SELECT * FROM brands WHERE slug = :slug AND is_deleted = false", nativeQuery = true)
    Optional<Brand> findBySlug(@Param("slug") String slug);

    @Query(value = "SELECT * FROM brands WHERE is_active = true AND is_deleted = false ORDER BY name", nativeQuery = true)
    List<Brand> findAllActive();
}
