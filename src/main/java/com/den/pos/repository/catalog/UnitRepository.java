package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    @Query(value = "SELECT * FROM units WHERE is_base_unit = true ORDER BY name", nativeQuery = true)
    List<Unit> findBaseUnits();

    @Query(value = "SELECT * FROM units WHERE base_unit_id = :baseUnitId", nativeQuery = true)
    List<Unit> findByBaseUnitId(@Param("baseUnitId") UUID baseUnitId);
}
