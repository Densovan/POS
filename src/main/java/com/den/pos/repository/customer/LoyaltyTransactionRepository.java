package com.den.pos.repository.customer;

import com.den.pos.entity.customer.LoyaltyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, UUID> {

    @Query(value = "SELECT * FROM loyalty_transactions WHERE customer_id = :customerId ORDER BY created_at DESC", nativeQuery = true)
    List<LoyaltyTransaction> findByCustomerId(@Param("customerId") UUID customerId);

    @Query(value = """
            SELECT COALESCE(SUM(points), 0) FROM loyalty_transactions
            WHERE customer_id = :customerId AND transaction_type = 'earned'
            """, nativeQuery = true)
    Integer sumEarnedPoints(@Param("customerId") UUID customerId);
}
