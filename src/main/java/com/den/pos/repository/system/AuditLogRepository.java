package com.den.pos.repository.system;

import com.den.pos.entity.system.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    @Query(value = "SELECT * FROM audit_logs WHERE user_id = :userId ORDER BY created_at DESC",
            countQuery = "SELECT COUNT(*) FROM audit_logs WHERE user_id = :userId",
            nativeQuery = true)
    Page<AuditLog> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = """
            SELECT * FROM audit_logs
            WHERE module = :module AND (:action IS NULL OR action = :action)
              AND created_at >= :from AND created_at < :to
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<AuditLog> findByModuleAndDateRange(@Param("module") String module,
                                            @Param("action") String action,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to);

    @Query(value = "SELECT * FROM audit_logs WHERE record_id = :recordId ORDER BY created_at DESC", nativeQuery = true)
    List<AuditLog> findByRecordId(@Param("recordId") UUID recordId);
}
