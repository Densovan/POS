package com.den.pos.entity.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_receipt_items", indexes = {
        @Index(name = "idx_purchase_receipt_items_product_id", columnList = "product_id"),
        @Index(name = "idx_purchase_receipt_items_purchase_order_item_id", columnList = "purchase_order_item_id"),
        @Index(name = "idx_purchase_receipt_items_purchase_receipt_id", columnList = "purchase_receipt_id"),
        @Index(name = "idx_purchase_receipt_items_variant_id", columnList = "variant_id")
})
public class PurchaseReceiptItem {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "purchase_receipt_id", nullable = false)
    private UUID purchaseReceiptId;

    @Column(name = "purchase_order_item_id")
    private UUID purchaseOrderItemId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity;

    @Column(name = "accepted_quantity", nullable = false)
    private Integer acceptedQuantity;

    @Column(name = "rejected_quantity")
    private Integer rejectedQuantity;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "unit_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "total_cost", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalCost;

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "serial_numbers", columnDefinition = "text[]")
    private String[] serialNumbers;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
