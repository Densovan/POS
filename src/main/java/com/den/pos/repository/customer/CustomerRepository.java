package com.den.pos.repository.customer;

import com.den.pos.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query(value = "SELECT * FROM customers WHERE phone = :phone AND is_deleted = false", nativeQuery = true)
    Optional<Customer> findByPhone(@Param("phone") String phone);

    @Query(value = """
            SELECT * FROM customers
            WHERE (first_name ILIKE CONCAT('%', :keyword, '%')
               OR last_name ILIKE CONCAT('%', :keyword, '%')
               OR phone ILIKE CONCAT('%', :keyword, '%'))
              AND is_deleted = false
            ORDER BY created_at DESC
            """, nativeQuery = true)
    List<Customer> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM customers WHERE loyalty_tier = :tier AND is_active = true AND is_deleted = false ORDER BY total_spent DESC", nativeQuery = true)
    List<Customer> findByLoyaltyTier(@Param("tier") String tier);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE customers
            SET loyalty_points = loyalty_points + :points,
                total_spent = total_spent + :amount,
                total_orders = total_orders + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = :customerId
            """, nativeQuery = true)
    int applyPurchase(@Param("customerId") UUID customerId,
                      @Param("points") Integer points,
                      @Param("amount") BigDecimal amount);
}
