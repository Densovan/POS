package com.den.pos.repository.sales;

import com.den.pos.entity.sales.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExchangeRepository extends JpaRepository<Exchange, UUID> {

    @Query(value = "SELECT * FROM exchanges WHERE original_sale_id = :originalSaleId ORDER BY created_at DESC", nativeQuery = true)
    List<Exchange> findByOriginalSaleId(@Param("originalSaleId") UUID originalSaleId);

    @Query(value = "SELECT * FROM exchanges WHERE new_sale_id = :newSaleId", nativeQuery = true)
    List<Exchange> findByNewSaleId(@Param("newSaleId") UUID newSaleId);
}
