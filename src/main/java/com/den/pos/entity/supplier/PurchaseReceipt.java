package com.den.pos.entity.supplier;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_receipts", indexes = {
        @Index(name = "idx_purchase_receipts_inspected_by", columnList = "inspected_by"),
        @Index(name = "idx_purchase_receipts_purchase_order_id", columnList = "purchase_order_id"),
        @Index(name = "idx_purchase_receipts_received_by", columnList = "received_by"),
        @Index(name = "idx_purchase_receipts_supplier_id", columnList = "supplier_id")
})
public class PurchaseReceipt {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "purchase_order_id")
    private UUID purchaseOrderId;

    @NaturalId
    @Column(name = "receipt_number", nullable = false, unique = true, length = 50)
    private String receiptNumber;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "supplier_invoice_number", length = 100)
    private String supplierInvoiceNumber;

    @Column(name = "received_by")
    private UUID receivedBy;

    @Column(name = "inspected_by")
    private UUID inspectedBy;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
