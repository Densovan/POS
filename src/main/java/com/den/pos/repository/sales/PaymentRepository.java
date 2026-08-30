package com.den.pos.repository.sales;

import com.den.pos.entity.sales.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query(value = "SELECT * FROM payments WHERE sale_id = :saleId AND is_deleted = false ORDER BY created_at", nativeQuery = true)
    List<Payment> findBySaleId(@Param("saleId") UUID saleId);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE sale_id = :saleId AND payment_status = 'completed' AND is_deleted = false", nativeQuery = true)
    BigDecimal sumCompletedAmountBySaleId(@Param("saleId") UUID saleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE payments
            SET refunded_amount = refunded_amount + :amount,
                refund_reference = :refundReference,
                refunded_at = CURRENT_TIMESTAMP,
                refunded_by = :refundedBy,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = :paymentId
            """, nativeQuery = true)
    int applyRefund(@Param("paymentId") UUID paymentId,
                    @Param("amount") BigDecimal amount,
                    @Param("refundReference") String refundReference,
                    @Param("refundedBy") UUID refundedBy);
}
