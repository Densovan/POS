package com.den.pos.repository.auth;

import com.den.pos.entity.auth.UserRole;
import com.den.pos.entity.auth.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query(value = "SELECT r.name FROM roles r JOIN user_roles ur ON ur.role_id = r.id WHERE ur.user_id = :userId", nativeQuery = true)
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);

    @Query(value = """
            SELECT ur.user_id AS userId, r.name AS roleName
            FROM user_roles ur
            JOIN roles r ON r.id = ur.role_id
            WHERE ur.user_id IN (:userIds)
            """, nativeQuery = true)
    List<UserRoleNameProjection> findRoleNamesByUserIds(@Param("userIds") Collection<UUID> userIds);

    @Query(value = """
            SELECT DISTINCT p.module || ':' || p.action
            FROM permissions p
            JOIN role_permissions rp ON rp.permission_id = p.id
            JOIN user_roles ur ON ur.role_id = rp.role_id
            WHERE ur.user_id = :userId
            """, nativeQuery = true)
    List<String> findPermissionKeysByUserId(@Param("userId") UUID userId);

    @Query(value = """
            SELECT ur.user_id FROM user_roles ur
            JOIN roles r ON r.id = ur.role_id
            WHERE r.name = :roleName LIMIT 1
            """, nativeQuery = true)
    Optional<UUID> findFirstUserIdWithRole(@Param("roleName") String roleName);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId) ON CONFLICT DO NOTHING", nativeQuery = true)
    int grant(@Param("userId") UUID userId, @Param("roleId") UUID roleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND role_id = :roleId", nativeQuery = true)
    int revoke(@Param("userId") UUID userId, @Param("roleId") UUID roleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId", nativeQuery = true)
    int revokeAllForUser(@Param("userId") UUID userId);
}
