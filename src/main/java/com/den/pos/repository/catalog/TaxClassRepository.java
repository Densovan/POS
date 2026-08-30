package com.den.pos.repository.catalog;

import com.den.pos.entity.catalog.TaxClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxClassRepository extends JpaRepository<TaxClass, UUID> {

    @Query(value = "SELECT * FROM tax_classes WHERE is_default = true AND is_deleted = false LIMIT 1", nativeQuery = true)
    Optional<TaxClass> findDefault();

    @Query(value = "SELECT * FROM tax_classes WHERE is_deleted = false ORDER BY rate", nativeQuery = true)
    List<TaxClass> findAllOrderByRate();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE tax_classes
            SET is_default = (id = :taxClassId), updated_at = CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int setDefault(@Param("taxClassId") UUID taxClassId);
}
