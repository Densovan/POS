package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    @Query(value = "SELECT * FROM stock_movements WHERE product_id = :productId ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM stock_movements WHERE product_id = :productId",
            nativeQuery = true)
    Page<StockMovement> findByProductId(@Param("productId") UUID productId, Pageable pageable);

    @Query(value = "SELECT * FROM stock_movements WHERE reference_type = :referenceType AND reference_id = :referenceId ORDER BY created_at", nativeQuery = true)
    List<StockMovement> findByReference(@Param("referenceType") String referenceType,
                                        @Param("referenceId") UUID referenceId);
}
