package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT * FROM products WHERE sku = :sku AND is_deleted = false", nativeQuery = true)
    Optional<Product> findBySku(@Param("sku") String sku);

    @Query(value = "SELECT * FROM products WHERE barcode = :barcode AND is_deleted = false", nativeQuery = true)
    Optional<Product> findByBarcode(@Param("barcode") String barcode);

    @Query(value = """
            SELECT * FROM products
            WHERE (name ILIKE CONCAT('%', :keyword, '%')
               OR sku ILIKE CONCAT('%', :keyword, '%')
               OR barcode ILIKE CONCAT('%', :keyword, '%'))
              AND is_active = true AND is_deleted = false
            """,
            countQuery = """
            SELECT COUNT(*) FROM products
            WHERE (name ILIKE CONCAT('%', :keyword, '%')
               OR sku ILIKE CONCAT('%', :keyword, '%')
               OR barcode ILIKE CONCAT('%', :keyword, '%'))
              AND is_active = true AND is_deleted = false
            """, nativeQuery = true)
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE category_id IN (SELECT id FROM categories WHERE parent_id = :categoryId OR id = :categoryId) AND is_active = true AND is_deleted = false", nativeQuery = true)
    List<Product> findByCategoryTree(@Param("categoryId") UUID categoryId);

    @Query(value = """
            SELECT p.* FROM products p
            LEFT JOIN inventory i ON i.product_id = p.id
            WHERE p.is_active = true AND p.is_track_stock = true AND p.is_deleted = false
            GROUP BY p.id
            HAVING COALESCE(SUM(i.current_stock), 0) <= COALESCE(MAX(p.reorder_point), 0)
            """, nativeQuery = true)
    List<Product> findReorderNeeded();

    @Query(value = "UPDATE products SET cost_price = :costPrice, updated_at = CURRENT_TIMESTAMP WHERE id = :productId RETURNING selling_price - :costPrice", nativeQuery = true)
    BigDecimal updateCostPrice(@Param("productId") UUID productId, @Param("costPrice") BigDecimal costPrice);
}
