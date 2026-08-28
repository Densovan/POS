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
@Table(name = "exchanges", indexes = {
        @Index(name = "idx_exchanges_customer_id", columnList = "customer_id"),
        @Index(name = "idx_exchanges_new_sale_id", columnList = "new_sale_id"),
        @Index(name = "idx_exchanges_original_sale_id", columnList = "original_sale_id"),
        @Index(name = "idx_exchanges_processed_by", columnList = "processed_by")
})
public class Exchange {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "original_sale_id")
    private UUID originalSaleId;

    @Column(name = "new_sale_id")
    private UUID newSaleId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "processed_by")
    private UUID processedBy;

    @Column(name = "exchange_type", length = 20)
    private String exchangeType;

    @Column(name = "price_difference", precision = 12, scale = 2)
    private BigDecimal priceDifference;

    @Column(name = "reason")
    private String reason;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
