package com.den.pos.repository.auth;

import com.den.pos.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    @Query(value = "SELECT * FROM permissions WHERE module = :module AND action = :action", nativeQuery = true)
    Optional<Permission> findByModuleAndAction(@Param("module") String module, @Param("action") String action);

    @Query(value = "SELECT * FROM permissions WHERE module = :module ORDER BY action", nativeQuery = true)
    List<Permission> findByModule(@Param("module") String module);
}
