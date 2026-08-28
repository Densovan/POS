package com.den.pos.repository.sales;

import com.den.pos.entity.sales.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {

    @Query(value = "SELECT * FROM sales WHERE invoice_number = :invoiceNumber AND is_deleted = false", nativeQuery = true)
    Optional<Sale> findByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    @Query(value = "SELECT * FROM sales WHERE customer_id = :customerId AND is_deleted = false ORDER BY sale_date DESC",
            countQuery = "SELECT COUNT(*) FROM sales WHERE customer_id = :customerId AND is_deleted = false",
            nativeQuery = true)
    Page<Sale> findByCustomerId(@Param("customerId") UUID customerId, Pageable pageable);

    @Query(value = "SELECT * FROM sales WHERE cashier_id = :cashierId AND sale_date >= :from AND sale_date < :to ORDER BY sale_date DESC", nativeQuery = true)
    List<Sale> findByCashierAndDateRange(@Param("cashierId") UUID cashierId,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to);

    @Query(value = """
            SELECT COALESCE(SUM(total), 0) FROM sales
            WHERE payment_status IN ('paid', 'partially_paid') AND is_deleted = false
              AND sale_date >= :from AND sale_date < :to
            """, nativeQuery = true)
    BigDecimal sumRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE sales
            SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, deleted_by = :deletedBy, updated_at = CURRENT_TIMESTAMP
            WHERE id = :saleId AND is_deleted = false
            """, nativeQuery = true)
    int softDelete(@Param("saleId") UUID saleId, @Param("deletedBy") UUID deletedBy);
}
