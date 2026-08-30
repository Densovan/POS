package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_counts", indexes = {
        @Index(name = "idx_stock_counts_counted_by", columnList = "counted_by"),
        @Index(name = "idx_stock_counts_expected_by", columnList = "expected_by"),
        @Index(name = "idx_stock_counts_warehouse_id", columnList = "warehouse_id")
})
@SoftDelete(columnName = "is_deleted")
public class StockCount extends BaseEntity {

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
}
