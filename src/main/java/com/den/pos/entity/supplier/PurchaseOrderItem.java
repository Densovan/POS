package com.den.pos.entity.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_order_items", indexes = {
        @Index(name = "idx_purchase_order_items_product_id", columnList = "product_id"),
        @Index(name = "idx_purchase_order_items_purchase_order_id", columnList = "purchase_order_id"),
        @Index(name = "idx_purchase_order_items_variant_id", columnList = "variant_id")
})
@SQLDelete(sql = """
        UPDATE purchase_order_items
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class PurchaseOrderItem extends BaseEntity {

    @Column(name = "purchase_order_id", nullable = false)
    private UUID purchaseOrderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "ordered_quantity", nullable = false)
    private Integer orderedQuantity;

    @Column(name = "received_quantity")
    private Integer receivedQuantity;

    @Column(name = "unit_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "total_cost", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalCost;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
