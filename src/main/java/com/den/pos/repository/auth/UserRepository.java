package com.den.pos.repository.auth;

import com.den.pos.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT * FROM users WHERE email = :email AND is_deleted = false", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE employee_id = :employeeId AND is_deleted = false", nativeQuery = true)
    Optional<User> findByEmployeeId(@Param("employeeId") UUID employeeId);

    @Query(value = """
            SELECT u.id          AS id,
                   u.email       AS email,
                   u.employee_id AS employeeId,
                   u.is_active   AS isActive,
                   e.first_name  AS firstName,
                   e.last_name   AS lastName,
                   e.position    AS position,
                   e.phone       AS phone
            FROM users u
            LEFT JOIN employees e ON e.user_id = u.id AND e.is_deleted = false
            WHERE u.id IN (:ids) AND u.is_deleted = false
            """, nativeQuery = true)
    List<UserInfoProjection> findUserInfoByIds(@Param("ids") Collection<UUID> ids);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE users
            SET last_login_at = :loginAt, login_attempts = 0, locked_until = NULL, updated_at = CURRENT_TIMESTAMP
            WHERE id = :userId
            """, nativeQuery = true)
    int recordSuccessfulLogin(@Param("userId") UUID userId, @Param("loginAt") LocalDateTime loginAt);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE users
            SET login_attempts = login_attempts + 1,
                locked_until = CASE WHEN login_attempts + 1 >= :maxAttempts THEN :lockedUntil ELSE locked_until END,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = :userId
            """, nativeQuery = true)
    int recordFailedLogin(@Param("userId") UUID userId,
                          @Param("maxAttempts") int maxAttempts,
                          @Param("lockedUntil") LocalDateTime lockedUntil);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE users SET refresh_token = :refreshToken, updated_at = CURRENT_TIMESTAMP WHERE id = :userId", nativeQuery = true)
    int updateRefreshToken(@Param("userId") UUID userId, @Param("refreshToken") String refreshToken);
}
