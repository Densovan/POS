package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    @Query(value = "SELECT * FROM product_images WHERE product_id = :productId ORDER BY display_order", nativeQuery = true)
    List<ProductImage> findByProductId(@Param("productId") UUID productId);

    @Query(value = "SELECT * FROM product_images WHERE product_id = :productId AND variant_id = :variantId ORDER BY display_order", nativeQuery = true)
    List<ProductImage> findByProductAndVariant(@Param("productId") UUID productId, @Param("variantId") UUID variantId);

    @Query(value = "SELECT * FROM product_images WHERE product_id = :productId AND is_primary = true LIMIT 1", nativeQuery = true)
    Optional<ProductImage> findPrimaryByProductId(@Param("productId") UUID productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE product_images SET is_primary = false WHERE product_id = :productId", nativeQuery = true)
    int clearPrimaryForProduct(@Param("productId") UUID productId);
}
