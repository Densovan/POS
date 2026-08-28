package com.den.pos.repository.system;

import com.den.pos.entity.system.SystemBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SystemBackupRepository extends JpaRepository<SystemBackup, UUID> {

    @Query(value = "SELECT * FROM system_backups WHERE backup_type = :backupType ORDER BY backup_date DESC", nativeQuery = true)
    List<SystemBackup> findByBackupType(@Param("backupType") String backupType);

    @Query(value = "SELECT COALESCE(SUM(file_size), 0) FROM system_backups WHERE restored_at IS NULL", nativeQuery = true)
    Long sumTotalBackupSize();
}
