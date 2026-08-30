package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Query(value = "SELECT * FROM purchase_orders WHERE po_number = :poNumber AND is_deleted = false", nativeQuery = true)
    Optional<PurchaseOrder> findByPoNumber(@Param("poNumber") String poNumber);

    @Query(value = "SELECT * FROM purchase_orders WHERE supplier_id = :supplierId AND is_deleted = false ORDER BY order_date DESC", nativeQuery = true)
    List<PurchaseOrder> findBySupplierId(@Param("supplierId") UUID supplierId);

    @Query(value = "SELECT * FROM purchase_orders WHERE status = :status AND is_deleted = false ORDER BY created_at DESC", nativeQuery = true)
    List<PurchaseOrder> findByStatus(@Param("status") String status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE purchase_orders
            SET status = 'received', actual_delivery_date = CURRENT_DATE, updated_at = CURRENT_TIMESTAMP
            WHERE id = :purchaseOrderId AND status IN ('draft', 'sent', 'confirmed')
            """, nativeQuery = true)
    int markReceived(@Param("purchaseOrderId") UUID purchaseOrderId);
}
