package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_count_items", indexes = {
        @Index(name = "idx_stock_count_items_inventory_id", columnList = "inventory_id"),
        @Index(name = "idx_stock_count_items_product_id", columnList = "product_id"),
        @Index(name = "idx_stock_count_items_stock_count_id", columnList = "stock_count_id"),
        @Index(name = "idx_stock_count_items_variant_id", columnList = "variant_id")
})
@SoftDelete(columnName = "is_deleted")
public class StockCountItem extends BaseEntity {

    @Column(name = "stock_count_id", nullable = false)
    private UUID stockCountId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "inventory_id")
    private UUID inventoryId;

    @Column(name = "expected_quantity", nullable = false)
    private Integer expectedQuantity;

    @Column(name = "counted_quantity")
    private Integer countedQuantity;

    @Column(name = "variance", insertable = false, updatable = false)
    private Integer variance;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
