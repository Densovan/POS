package com.den.pos.repository.hr;

import com.den.pos.entity.hr.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    @Query(value = "SELECT * FROM shifts WHERE employee_id = :employeeId AND status = 'active' ORDER BY start_time DESC LIMIT 1", nativeQuery = true)
    Optional<Shift> findActiveShiftByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(value = "SELECT * FROM shifts WHERE employee_id = :employeeId AND shift_date >= :from AND shift_date <= :to ORDER BY shift_date DESC", nativeQuery = true)
    List<Shift> findByEmployeeAndDateRange(@Param("employeeId") UUID employeeId,
                                           @Param("from") LocalDateTime from,
                                           @Param("to") LocalDateTime to);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE shifts
            SET end_time = :endTime,
                cash_end_amount = :cashEndAmount,
                cash_expected_amount = :cashExpectedAmount,
                cash_difference = :cashExpectedAmount - :cashEndAmount,
                status = 'closed',
                updated_at = CURRENT_TIMESTAMP
            WHERE id = :shiftId AND status = 'active'
            """, nativeQuery = true)
    int closeShift(@Param("shiftId") UUID shiftId,
                   @Param("endTime") LocalDateTime endTime,
                   @Param("cashEndAmount") BigDecimal cashEndAmount,
                   @Param("cashExpectedAmount") BigDecimal cashExpectedAmount);
}
