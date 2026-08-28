package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.StockCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StockCountRepository extends JpaRepository<StockCount, UUID> {

    @Query(value = "SELECT * FROM stock_counts WHERE status = :status ORDER BY count_date DESC", nativeQuery = true)
    List<StockCount> findByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM stock_counts WHERE warehouse_id = :warehouseId ORDER BY count_date DESC", nativeQuery = true)
    List<StockCount> findByWarehouseId(@Param("warehouseId") UUID warehouseId);
}
