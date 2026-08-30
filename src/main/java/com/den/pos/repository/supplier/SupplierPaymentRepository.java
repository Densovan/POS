package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, UUID> {

    @Query(value = "SELECT * FROM supplier_payments WHERE supplier_id = :supplierId AND is_deleted = false ORDER BY payment_date DESC", nativeQuery = true)
    List<SupplierPayment> findBySupplierId(@Param("supplierId") UUID supplierId);

    @Query(value = "SELECT * FROM supplier_payments WHERE purchase_order_id = :purchaseOrderId AND is_deleted = false ORDER BY payment_date DESC", nativeQuery = true)
    List<SupplierPayment> findByPurchaseOrderId(@Param("purchaseOrderId") UUID purchaseOrderId);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM supplier_payments WHERE purchase_order_id = :purchaseOrderId AND is_deleted = false", nativeQuery = true)
    BigDecimal sumPaidAmountByPurchaseOrderId(@Param("purchaseOrderId") UUID purchaseOrderId);
}
