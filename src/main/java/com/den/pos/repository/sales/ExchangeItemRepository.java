package com.den.pos.repository.sales;

import com.den.pos.entity.sales.ExchangeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExchangeItemRepository extends JpaRepository<ExchangeItem, UUID> {

    @Query(value = "SELECT * FROM exchange_items WHERE exchange_id = :exchangeId", nativeQuery = true)
    List<ExchangeItem> findByExchangeId(@Param("exchangeId") UUID exchangeId);
}
