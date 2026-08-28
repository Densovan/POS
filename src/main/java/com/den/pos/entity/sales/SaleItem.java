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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sale_items", indexes = {
        @Index(name = "idx_sale_items_batch_id", columnList = "batch_id"),
        @Index(name = "idx_sale_items_product_id", columnList = "product_id"),
        @Index(name = "idx_sale_items_sale_id", columnList = "sale_id"),
        @Index(name = "idx_sale_items_serial_number_id", columnList = "serial_number_id"),
        @Index(name = "idx_sale_items_variant_id", columnList = "variant_id")
})
public class SaleItem {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "sale_id", nullable = false)
    private UUID saleId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "variant_attributes", columnDefinition = "jsonb")
    private String variantAttributes;

    @Column(name = "sku", length = 100)
    private String sku;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal total;

    @Column(name = "batch_id")
    private UUID batchId;

    @Column(name = "serial_number_id")
    private UUID serialNumberId;

    @Column(name = "is_returned")
    private Boolean isReturned;

    @Column(name = "returned_quantity")
    private Integer returnedQuantity;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
