package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_product_variants_product_id", columnList = "product_id")
})
@SoftDelete(columnName = "is_deleted")
public class ProductVariant extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @NaturalId
    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", nullable = false, columnDefinition = "jsonb")
    private String attributes;

    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    @Column(name = "reorder_point")
    private Integer reorderPoint;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_track_stock")
    private Boolean isTrackStock;

    @Column(name = "is_serialized")
    private Boolean isSerialized;

    @Column(name = "has_batches")
    private Boolean hasBatches;
}
