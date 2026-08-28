package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    @Query(value = "SELECT * FROM warehouses WHERE code = :code", nativeQuery = true)
    Optional<Warehouse> findByCode(@Param("code") String code);

    @Query(value = "SELECT * FROM warehouses WHERE is_active = true ORDER BY name", nativeQuery = true)
    List<Warehouse> findAllActive();
}
