package com.den.pos.entity.customer;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "loyalty_transactions", indexes = {
        @Index(name = "idx_loyalty_transactions_created_by", columnList = "created_by"),
        @Index(name = "idx_loyalty_transactions_customer_id", columnList = "customer_id"),
        @Index(name = "idx_loyalty_transactions_sale_id", columnList = "sale_id")
})
public class LoyaltyTransaction {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "sale_id")
    private UUID saleId;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "balance_before", nullable = false)
    private Integer balanceBefore;

    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
