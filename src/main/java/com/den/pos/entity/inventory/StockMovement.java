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
@Table(name = "stock_movements", indexes = {
        @Index(name = "idx_stock_movements_batch_id", columnList = "batch_id"),
        @Index(name = "idx_stock_movements_inventory_id", columnList = "inventory_id"),
        @Index(name = "idx_stock_movements_performed_by", columnList = "performed_by"),
        @Index(name = "idx_stock_movements_product_id", columnList = "product_id"),
        @Index(name = "idx_stock_movements_variant_id", columnList = "variant_id")
})
public class StockMovement {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "inventory_id")
    private UUID inventoryId;

    @Column(name = "batch_id")
    private UUID batchId;

    @Column(name = "movement_type", nullable = false, length = 20)
    private String movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "previous_quantity")
    private Integer previousQuantity;

    @Column(name = "new_quantity")
    private Integer newQuantity;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "performed_by")
    private UUID performedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
