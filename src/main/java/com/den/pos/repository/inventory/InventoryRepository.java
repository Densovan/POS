package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    @Query(value = "SELECT * FROM inventory WHERE product_id = :productId AND is_deleted = false", nativeQuery = true)
    List<Inventory> findByProductId(@Param("productId") UUID productId);

    @Query(value = "SELECT * FROM inventory WHERE warehouse_id = :warehouseId AND is_deleted = false", nativeQuery = true)
    List<Inventory> findByWarehouseId(@Param("warehouseId") UUID warehouseId);

    @Query(value = """
            SELECT * FROM inventory
            WHERE product_id = :productId AND variant_id IS NOT DISTINCT FROM :variantId AND warehouse_id IS NOT DISTINCT FROM :warehouseId
              AND is_deleted = false
            """, nativeQuery = true)
    Optional<Inventory> findByProductVariantAndWarehouse(@Param("productId") UUID productId,
                                                         @Param("variantId") UUID variantId,
                                                         @Param("warehouseId") UUID warehouseId);

    @Query(value = "SELECT * FROM inventory WHERE current_stock <= minimum_stock AND is_deleted = false ORDER BY (current_stock - minimum_stock)", nativeQuery = true)
    List<Inventory> findLowStock();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT * FROM inventory WHERE id = :id AND is_deleted = false FOR UPDATE", nativeQuery = true)
    Optional<Inventory> findByIdForUpdate(@Param("id") UUID id);
}
