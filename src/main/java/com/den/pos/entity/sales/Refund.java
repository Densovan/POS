package com.den.pos.entity.sales;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "refunds", indexes = {
        @Index(name = "idx_refunds_customer_id", columnList = "customer_id"),
        @Index(name = "idx_refunds_original_sale_id", columnList = "original_sale_id"),
        @Index(name = "idx_refunds_processed_by", columnList = "processed_by")
})
public class Refund {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "original_sale_id")
    private UUID originalSaleId;

    @NaturalId
    @Column(name = "refund_invoice_number", nullable = false, unique = true, length = 50)
    private String refundInvoiceNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "processed_by")
    private UUID processedBy;

    @Column(name = "refund_type", length = 20)
    private String refundType;

    @Column(name = "refund_method", nullable = false, length = 30)
    private String refundMethod;

    @Column(name = "total_refund_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalRefundAmount;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
