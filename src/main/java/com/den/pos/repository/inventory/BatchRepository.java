package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BatchRepository extends JpaRepository<Batch, UUID> {

    @Query(value = "SELECT * FROM batches WHERE product_id = :productId AND current_quantity > 0 ORDER BY expiry_date", nativeQuery = true)
    List<Batch> findAvailableByProductId(@Param("productId") UUID productId);

    @Query(value = "SELECT * FROM batches WHERE expiry_date <= :date AND current_quantity > 0 ORDER BY expiry_date", nativeQuery = true)
    List<Batch> findExpiringBefore(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM batches WHERE inventory_id = :inventoryId AND is_active = true ORDER BY expiry_date", nativeQuery = true)
    List<Batch> findByInventoryId(@Param("inventoryId") UUID inventoryId);
}
