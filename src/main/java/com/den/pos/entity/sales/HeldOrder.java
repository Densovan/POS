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

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "held_orders", indexes = {
        @Index(name = "idx_held_orders_cashier_id", columnList = "cashier_id"),
        @Index(name = "idx_held_orders_customer_id", columnList = "customer_id"),
        @Index(name = "idx_held_orders_sale_id", columnList = "sale_id")
})
public class HeldOrder {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "sale_id")
    private UUID saleId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "cashier_id")
    private UUID cashierId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cart_data", nullable = false, columnDefinition = "jsonb")
    private String cartData;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resumed_at")
    private LocalDateTime resumedAt;

    @Column(name = "is_resumed")
    private Boolean isResumed;
}
