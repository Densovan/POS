package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.StockCountItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StockCountItemRepository extends JpaRepository<StockCountItem, UUID> {

    @Query(value = "SELECT * FROM stock_count_items WHERE stock_count_id = :stockCountId", nativeQuery = true)
    List<StockCountItem> findByStockCountId(@Param("stockCountId") UUID stockCountId);

    @Query(value = "SELECT * FROM stock_count_items WHERE stock_count_id = :stockCountId AND variance <> 0", nativeQuery = true)
    List<StockCountItem> findVarianceItems(@Param("stockCountId") UUID stockCountId);
}
