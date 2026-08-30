package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "serial_numbers", indexes = {
        @Index(name = "idx_serial_numbers_batch_id", columnList = "batch_id"),
        @Index(name = "idx_serial_numbers_customer_id", columnList = "customer_id"),
        @Index(name = "idx_serial_numbers_product_id", columnList = "product_id"),
        @Index(name = "idx_serial_numbers_sale_id", columnList = "sale_id"),
        @Index(name = "idx_serial_numbers_sale_item_id", columnList = "sale_item_id"),
        @Index(name = "idx_serial_numbers_variant_id", columnList = "variant_id")
})
@SQLDelete(sql = """
        UPDATE serial_numbers
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class SerialNumber extends BaseEntity {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "batch_id")
    private UUID batchId;

    @NaturalId
    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Column(name = "imei", length = 100)
    private String imei;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "sale_id")
    private UUID saleId;

    @Column(name = "sale_item_id")
    private UUID saleItemId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "warranty_start_date")
    private LocalDate warrantyStartDate;

    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
