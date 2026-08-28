package com.den.pos.repository.auth;

import com.den.pos.entity.auth.RolePermission;
import com.den.pos.entity.auth.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    @Query(value = "SELECT * FROM role_permissions WHERE role_id = :roleId", nativeQuery = true)
    List<RolePermission> findByRoleId(@Param("roleId") UUID roleId);

    @Query(value = """
            SELECT p.* FROM permissions p
            JOIN role_permissions rp ON rp.permission_id = p.id
            WHERE rp.role_id = :roleId
            ORDER BY p.module, p.action
            """, nativeQuery = true)
    List<Object[]> findPermissionsByRoleId(@Param("roleId") UUID roleId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            INSERT INTO role_permissions (role_id, permission_id)
            VALUES (:roleId, :permissionId)
            ON CONFLICT DO NOTHING
            """, nativeQuery = true)
    int grant(@Param("roleId") UUID roleId, @Param("permissionId") UUID permissionId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM role_permissions WHERE role_id = :roleId AND permission_id = :permissionId", nativeQuery = true)
    int revoke(@Param("roleId") UUID roleId, @Param("permissionId") UUID permissionId);
}
