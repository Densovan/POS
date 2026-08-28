package com.den.pos.entity.inventory;

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
@Table(name = "stock_counts", indexes = {
        @Index(name = "idx_stock_counts_counted_by", columnList = "counted_by"),
        @Index(name = "idx_stock_counts_expected_by", columnList = "expected_by"),
        @Index(name = "idx_stock_counts_warehouse_id", columnList = "warehouse_id")
})
public class StockCount {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "count_date", nullable = false)
    private LocalDate countDate;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Column(name = "expected_by")
    private UUID expectedBy;

    @Column(name = "counted_by")
    private UUID countedBy;

    @Column(name = "counted_at")
    private LocalDateTime countedAt;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
