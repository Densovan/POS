package com.den.pos.repository.auth;

import com.den.pos.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query(value = "SELECT * FROM roles WHERE name = :name", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM roles WHERE is_system_role = true ORDER BY name", nativeQuery = true)
    List<Role> findAllSystemRoles();
}
