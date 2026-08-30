package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.PurchaseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, UUID> {

    @Query(value = "SELECT * FROM purchase_receipts WHERE receipt_number = :receiptNumber AND is_deleted = false", nativeQuery = true)
    Optional<PurchaseReceipt> findByReceiptNumber(@Param("receiptNumber") String receiptNumber);

    @Query(value = "SELECT * FROM purchase_receipts WHERE purchase_order_id = :purchaseOrderId AND is_deleted = false ORDER BY receipt_date DESC", nativeQuery = true)
    List<PurchaseReceipt> findByPurchaseOrderId(@Param("purchaseOrderId") UUID purchaseOrderId);

    @Query(value = "SELECT * FROM purchase_receipts WHERE supplier_id = :supplierId AND is_deleted = false ORDER BY receipt_date DESC", nativeQuery = true)
    List<PurchaseReceipt> findBySupplierId(@Param("supplierId") UUID supplierId);
}
