package com.den.pos.repository.sales;

import com.den.pos.entity.sales.HeldOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface HeldOrderRepository extends JpaRepository<HeldOrder, UUID> {

    @Query(value = "SELECT * FROM held_orders WHERE cashier_id = :cashierId AND is_resumed = false AND expiry_time > :now ORDER BY created_at DESC", nativeQuery = true)
    List<HeldOrder> findActiveByCashierId(@Param("cashierId") UUID cashierId, @Param("now") LocalDateTime now);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE held_orders
            SET is_resumed = true, resumed_at = :resumedAt
            WHERE id = :heldOrderId AND is_resumed = false
            """, nativeQuery = true)
    int markResumed(@Param("heldOrderId") UUID heldOrderId, @Param("resumedAt") LocalDateTime resumedAt);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM held_orders WHERE expiry_time < :now AND is_resumed = false", nativeQuery = true)
    int deleteExpired(@Param("now") LocalDateTime now);
}
