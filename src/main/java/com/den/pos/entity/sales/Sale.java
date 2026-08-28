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
@Table(name = "sales", indexes = {
        @Index(name = "idx_sales_cashier_id", columnList = "cashier_id"),
        @Index(name = "idx_sales_customer_id", columnList = "customer_id"),
        @Index(name = "idx_sales_deleted_by", columnList = "deleted_by")
})
public class Sale {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @NaturalId
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone", length = 20)
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "cashier_id")
    private UUID cashierId;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @Column(name = "sale_type", length = 20)
    private String saleType;

    @Column(name = "order_type", length = 20)
    private String orderType;

    @Column(name = "table_number", length = 20)
    private String tableNumber;

    @Column(name = "service_type", length = 20)
    private String serviceType;

    @Column(name = "guest_count")
    private Integer guestCount;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_type", length = 20)
    private String discountType;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_reason", columnDefinition = "text")
    private String discountReason;

    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Column(name = "order_status", length = 20)
    private String orderStatus;

    @Column(name = "customer_notes", columnDefinition = "text")
    private String customerNotes;

    @Column(name = "internal_notes", columnDefinition = "text")
    private String internalNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;
}
