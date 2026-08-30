package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "batches", indexes = {
        @Index(name = "idx_batches_inventory_id", columnList = "inventory_id"),
        @Index(name = "idx_batches_product_id", columnList = "product_id"),
        @Index(name = "idx_batches_purchase_receipt_id", columnList = "purchase_receipt_id"),
        @Index(name = "idx_batches_supplier_id", columnList = "supplier_id"),
        @Index(name = "idx_batches_variant_id", columnList = "variant_id")
})
@SQLDelete(sql = """
        UPDATE batches
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class Batch extends BaseEntity {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "inventory_id")
    private UUID inventoryId;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Column(name = "manufacturing_date")
    private LocalDate manufacturingDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity;

    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity;

    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "purchase_receipt_id")
    private UUID purchaseReceiptId;

    @Column(name = "is_active")
    private Boolean isActive;
}
