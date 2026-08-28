package com.den.pos.repository.hr;

import com.den.pos.entity.hr.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query(value = "SELECT * FROM attendance WHERE employee_id = :employeeId AND clock_out IS NULL ORDER BY clock_in DESC LIMIT 1", nativeQuery = true)
    Optional<Attendance> findOpenAttendanceByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(value = "SELECT * FROM attendance WHERE employee_id = :employeeId AND clock_in >= :from AND clock_in < :to ORDER BY clock_in DESC", nativeQuery = true)
    List<Attendance> findByEmployeeAndDateRange(@Param("employeeId") UUID employeeId,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE attendance
            SET clock_out = :clockOut,
                total_hours = ROUND(EXTRACT(EPOCH FROM (:clockOut - clock_in)) / 3600.0, 2),
                break_start = COALESCE(break_start, :breakStart),
                break_end = COALESCE(break_end, :breakEnd),
                status = CASE WHEN status = 'late' THEN 'late' ELSE 'present' END
            WHERE id = :attendanceId AND clock_out IS NULL
            """, nativeQuery = true)
    int clockOut(@Param("attendanceId") UUID attendanceId,
                 @Param("clockOut") LocalDateTime clockOut,
                 @Param("breakStart") LocalDateTime breakStart,
                 @Param("breakEnd") LocalDateTime breakEnd);
}
