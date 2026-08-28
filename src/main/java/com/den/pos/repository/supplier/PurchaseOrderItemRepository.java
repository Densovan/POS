package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, UUID> {

    @Query(value = "SELECT * FROM purchase_order_items WHERE purchase_order_id = :purchaseOrderId", nativeQuery = true)
    List<PurchaseOrderItem> findByPurchaseOrderId(@Param("purchaseOrderId") UUID purchaseOrderId);

    @Query(value = "SELECT * FROM purchase_order_items WHERE product_id = :productId", nativeQuery = true)
    List<PurchaseOrderItem> findByProductId(@Param("productId") UUID productId);

    @Query(value = """
            SELECT COALESCE(SUM(ordered_quantity - received_quantity), 0)
            FROM purchase_order_items
            WHERE variant_id = :variantId
              AND purchase_order_id IN (SELECT id FROM purchase_orders WHERE status NOT IN ('received', 'cancelled'))
            """, nativeQuery = true)
    Integer sumPendingQuantityForVariant(@Param("variantId") UUID variantId);
}
