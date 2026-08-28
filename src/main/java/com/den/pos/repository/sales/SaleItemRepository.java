package com.den.pos.repository.sales;

import com.den.pos.entity.sales.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {

    @Query(value = "SELECT * FROM sale_items WHERE sale_id = :saleId", nativeQuery = true)
    List<SaleItem> findBySaleId(@Param("saleId") UUID saleId);

    @Query(value = "SELECT * FROM sale_items WHERE batch_id = :batchId", nativeQuery = true)
    List<SaleItem> findByBatchId(@Param("batchId") UUID batchId);

    @Query(value = """
            SELECT si.product_id, si.product_name, SUM(si.quantity) AS total_sold
            FROM sale_items si
            JOIN sales s ON s.id = si.sale_id
            WHERE s.sale_date >= :from AND s.sale_date < :to AND s.is_deleted = false
            GROUP BY si.product_id, si.product_name
            ORDER BY total_sold DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findBestSellers(@Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to,
                                   @Param("limit") int limit);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE sale_items
            SET returned_quantity = returned_quantity + :quantity,
                is_returned = CASE WHEN returned_quantity + :quantity >= quantity THEN true ELSE is_returned END,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = :saleItemId
            """, nativeQuery = true)
    int applyReturn(@Param("saleItemId") UUID saleItemId, @Param("quantity") Integer quantity);
}
