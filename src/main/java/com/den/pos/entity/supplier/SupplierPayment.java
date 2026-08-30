package com.den.pos.entity.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "supplier_payments", indexes = {
        @Index(name = "idx_supplier_payments_created_by", columnList = "created_by"),
        @Index(name = "idx_supplier_payments_purchase_order_id", columnList = "purchase_order_id"),
        @Index(name = "idx_supplier_payments_supplier_id", columnList = "supplier_id")
})
@SQLDelete(sql = """
        UPDATE supplier_payments
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class SupplierPayment extends BaseEntity {

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "purchase_order_id")
    private UUID purchaseOrderId;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "check_number", length = 100)
    private String checkNumber;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
