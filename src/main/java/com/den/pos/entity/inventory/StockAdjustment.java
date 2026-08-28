package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_adjustments", indexes = {
        @Index(name = "idx_stock_adjustments_approved_by", columnList = "approved_by"),
        @Index(name = "idx_stock_adjustments_created_by", columnList = "created_by"),
        @Index(name = "idx_stock_adjustments_inventory_id", columnList = "inventory_id"),
        @Index(name = "idx_stock_adjustments_product_id", columnList = "product_id"),
        @Index(name = "idx_stock_adjustments_variant_id", columnList = "variant_id")
})
public class StockAdjustment {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "inventory_id")
    private UUID inventoryId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "adjustment_type", nullable = false, length = 20)
    private String adjustmentType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reason", nullable = false, length = 50)
    private String reason;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
