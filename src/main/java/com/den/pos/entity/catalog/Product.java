package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_brand_id", columnList = "brand_id"),
        @Index(name = "idx_products_category_id", columnList = "category_id"),
        @Index(name = "idx_products_tax_class_id", columnList = "tax_class_id"),
        @Index(name = "idx_products_unit_id", columnList = "unit_id")
})
@SQLDelete(sql = """
        UPDATE products
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @NaturalId
    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "barcode", length = 100)
    private String barcode;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "tax_class_id")
    private UUID taxClassId;

    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "wholesale_price", precision = 10, scale = 2)
    private BigDecimal wholesalePrice;

    @Column(name = "profit_margin", precision = 5, scale = 2, insertable = false, updatable = false)
    private BigDecimal profitMargin;

    @Column(name = "min_stock_level")
    private Integer minStockLevel;

    @Column(name = "max_stock_level")
    private Integer maxStockLevel;

    @Column(name = "reorder_point")
    private Integer reorderPoint;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "weight_unit", length = 10)
    private String weightUnit;

    @Column(name = "dimensions", length = 100)
    private String dimensions;

    @Column(name = "is_variant_parent")
    private Boolean isVariantParent;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_track_stock")
    private Boolean isTrackStock;

    @Column(name = "is_serialized")
    private Boolean isSerialized;

    @Column(name = "has_batches")
    private Boolean hasBatches;

    @Column(name = "main_image_url", columnDefinition = "text")
    private String mainImageUrl;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description", columnDefinition = "text")
    private String metaDescription;

    @Column(name = "meta_keywords", columnDefinition = "text")
    private String metaKeywords;
}
