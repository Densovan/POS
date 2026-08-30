package com.den.pos.entity.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_refunded_by", columnList = "refunded_by"),
        @Index(name = "idx_payments_sale_id", columnList = "sale_id")
})
@SQLDelete(sql = """
        UPDATE payments
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class Payment extends BaseEntity {

    @Column(name = "sale_id", nullable = false)
    private UUID saleId;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "payment_type", length = 20)
    private String paymentType;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", length = 20)
    private String cardBrand;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "qr_code_data", columnDefinition = "text")
    private String qrCodeData;

    @Column(name = "payment_provider", length = 50)
    private String paymentProvider;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Column(name = "refunded_amount", precision = 12, scale = 2)
    private BigDecimal refundedAmount;

    @Column(name = "refund_reference", length = 100)
    private String refundReference;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    @Column(name = "refunded_by")
    private UUID refundedBy;
}
