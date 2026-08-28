package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
public class Product {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
