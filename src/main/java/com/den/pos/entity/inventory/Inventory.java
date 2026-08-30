package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "idx_inventory_last_counted_by", columnList = "last_counted_by"),
        @Index(name = "idx_inventory_variant_id", columnList = "variant_id"),
        @Index(name = "idx_inventory_warehouse_id", columnList = "warehouse_id")
})
@SQLDelete(sql = """
        UPDATE inventory
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class Inventory extends BaseEntity {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "current_stock")
    private Integer currentStock;

    @Column(name = "available_stock")
    private Integer availableStock;

    @Column(name = "reserved_stock")
    private Integer reservedStock;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @Column(name = "maximum_stock")
    private Integer maximumStock;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Column(name = "shelf_location", length = 50)
    private String shelfLocation;

    @Column(name = "last_count_date")
    private LocalDate lastCountDate;

    @Column(name = "last_counted_by")
    private UUID lastCountedBy;
}
