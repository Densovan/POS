package com.den.pos.repository.sales;

import com.den.pos.entity.sales.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {

    @Query(value = "SELECT * FROM refunds WHERE refund_invoice_number = :refundInvoiceNumber", nativeQuery = true)
    Optional<Refund> findByRefundInvoiceNumber(@Param("refundInvoiceNumber") String refundInvoiceNumber);

    @Query(value = "SELECT * FROM refunds WHERE original_sale_id = :originalSaleId ORDER BY created_at DESC", nativeQuery = true)
    List<Refund> findByOriginalSaleId(@Param("originalSaleId") UUID originalSaleId);

    @Query(value = """
            SELECT COALESCE(SUM(total_refund_amount), 0) FROM refunds
            WHERE processed_at >= :from AND processed_at < :to
            """, nativeQuery = true)
    java.math.BigDecimal sumRefundedAmountBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
