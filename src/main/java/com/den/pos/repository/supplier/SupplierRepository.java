package com.den.pos.repository.supplier;

import com.den.pos.entity.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    @Query(value = "SELECT * FROM suppliers WHERE name ILIKE CONCAT('%', :keyword, '%') OR company_name ILIKE CONCAT('%', :keyword, '%') OR phone = :keyword", nativeQuery = true)
    List<Supplier> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM suppliers WHERE is_active = true ORDER BY name", nativeQuery = true)
    List<Supplier> findAllActive();

    @Query(value = "SELECT COALESCE(SUM(outstanding_balance), 0) FROM suppliers WHERE id = :supplierId", nativeQuery = true)
    java.math.BigDecimal sumOutstandingBalance(@Param("supplierId") UUID supplierId);
}
