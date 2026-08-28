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
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exchange_items", indexes = {
        @Index(name = "idx_exchange_items_exchange_id", columnList = "exchange_id"),
        @Index(name = "idx_exchange_items_new_product_id", columnList = "new_product_id"),
        @Index(name = "idx_exchange_items_new_variant_id", columnList = "new_variant_id"),
        @Index(name = "idx_exchange_items_returned_sale_item_id", columnList = "returned_sale_item_id")
})
public class ExchangeItem {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "exchange_id", nullable = false)
    private UUID exchangeId;

    @Column(name = "returned_sale_item_id")
    private UUID returnedSaleItemId;

    @Column(name = "returned_quantity", nullable = false)
    private Integer returnedQuantity;

    @Column(name = "new_product_id")
    private UUID newProductId;

    @Column(name = "new_variant_id")
    private UUID newVariantId;

    @Column(name = "new_quantity", nullable = false)
    private Integer newQuantity;

    @Column(name = "new_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal newUnitPrice;

    @Column(name = "price_adjustment", precision = 12, scale = 2)
    private BigDecimal priceAdjustment;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
