package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.PurchaseReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PurchaseReceiptItemRepository extends JpaRepository<PurchaseReceiptItem, UUID> {

    @Query(value = "SELECT * FROM purchase_receipt_items WHERE purchase_receipt_id = :purchaseReceiptId", nativeQuery = true)
    List<PurchaseReceiptItem> findByPurchaseReceiptId(@Param("purchaseReceiptId") UUID purchaseReceiptId);

    @Query(value = """
            SELECT * FROM purchase_receipt_items
            WHERE rejected_quantity > 0 AND product_id = :productId
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<PurchaseReceiptItem> findRejectedByProductId(@Param("productId") UUID productId);
}
