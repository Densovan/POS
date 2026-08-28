package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, UUID> {

    @Query(value = "SELECT * FROM stock_adjustments WHERE approved_by IS NULL ORDER BY created_at DESC", nativeQuery = true)
    List<StockAdjustment> findPending();

    @Query(value = "SELECT * FROM stock_adjustments WHERE product_id = :productId ORDER BY created_at DESC", nativeQuery = true)
    List<StockAdjustment> findByProductId(@Param("productId") UUID productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE stock_adjustments
            SET approved_by = :approvedBy, approved_at = :approvedAt, updated_at = CURRENT_TIMESTAMP
            WHERE id = :adjustmentId AND approved_by IS NULL
            """, nativeQuery = true)
    int approve(@Param("adjustmentId") UUID adjustmentId,
                @Param("approvedBy") UUID approvedBy,
                @Param("approvedAt") LocalDateTime approvedAt);
}
