package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@SQLDelete(sql = """
        UPDATE stock_adjustments
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class StockAdjustment extends BaseEntity {

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
}
