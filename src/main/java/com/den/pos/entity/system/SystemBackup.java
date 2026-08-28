package com.den.pos.entity.system;

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

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_backups", indexes = {
        @Index(name = "idx_system_backups_created_by", columnList = "created_by"),
        @Index(name = "idx_system_backups_restored_by", columnList = "restored_by")
})
public class SystemBackup {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "backup_type", nullable = false, length = 20)
    private String backupType;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false, columnDefinition = "text")
    private String filePath;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "backup_date")
    private LocalDateTime backupDate;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @Column(name = "restored_by")
    private UUID restoredBy;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
