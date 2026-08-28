package com.den.pos.repository.sales;

import com.den.pos.entity.sales.RefundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RefundItemRepository extends JpaRepository<RefundItem, UUID> {

    @Query(value = "SELECT * FROM refund_items WHERE refund_id = :refundId", nativeQuery = true)
    List<RefundItem> findByRefundId(@Param("refundId") UUID refundId);

    @Query(value = "SELECT * FROM refund_items WHERE original_sale_item_id = :originalSaleItemId", nativeQuery = true)
    List<RefundItem> findByOriginalSaleItemId(@Param("originalSaleItemId") UUID originalSaleItemId);
}
