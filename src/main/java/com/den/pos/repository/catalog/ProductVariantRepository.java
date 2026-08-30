package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    @Query(value = "SELECT * FROM product_variants WHERE product_id = :productId AND is_deleted = false", nativeQuery = true)
    List<ProductVariant> findByProductId(@Param("productId") UUID productId);

    @Query(value = "SELECT * FROM product_variants WHERE sku = :sku AND is_deleted = false", nativeQuery = true)
    Optional<ProductVariant> findBySku(@Param("sku") String sku);

    @Query(value = "SELECT * FROM product_variants WHERE barcode = :barcode AND is_deleted = false", nativeQuery = true)
    Optional<ProductVariant> findByBarcode(@Param("barcode") String barcode);
}
