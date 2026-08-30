package com.den.pos.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_employee_id", columnList = "employee_id"),
        @Index(name = "idx_users_is_active", columnList = "is_active")
})
@SQLDelete(sql = """
        UPDATE users
        SET is_deleted = true, updated_at = CURRENT_TIMESTAMP
        WHERE id = ? AND version = ?
        """)
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Column(name = "employee_id")
    private UUID employeeId;

    @NaturalId

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @JsonIgnore
    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @JsonIgnore
    @Column(name = "refresh_token", length = 500)
    private String refreshToken;
}
