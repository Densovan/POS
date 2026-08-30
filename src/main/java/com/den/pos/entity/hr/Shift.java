package com.den.pos.entity.hr;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shifts", indexes = {
        @Index(name = "idx_shifts_employee_id", columnList = "employee_id")
})
@SQLDelete(sql = """
        UPDATE shifts
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class Shift extends BaseEntity {

    @Column(name = "employee_id")
    private UUID employeeId;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "expected_end_time")
    private LocalDateTime expectedEndTime;

    @Column(name = "cash_start_amount", precision = 10, scale = 2)
    private BigDecimal cashStartAmount;

    @Column(name = "cash_end_amount", precision = 10, scale = 2)
    private BigDecimal cashEndAmount;

    @Column(name = "cash_expected_amount", precision = 10, scale = 2)
    private BigDecimal cashExpectedAmount;

    @Column(name = "cash_difference", precision = 10, scale = 2)
    private BigDecimal cashDifference;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
