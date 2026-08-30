package com.den.pos.repository.expense;

import com.den.pos.entity.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query(value = "SELECT * FROM expenses WHERE category_id = :categoryId AND is_deleted = false ORDER BY expense_date DESC", nativeQuery = true)
    List<Expense> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query(value = """
            SELECT * FROM expenses
            WHERE expense_date >= :from AND expense_date < :to
              AND (:status IS NULL OR status = :status)
              AND is_deleted = false
            ORDER BY expense_date DESC
            """, nativeQuery = true)
    List<Expense> findByDateRangeAndStatus(@Param("from") LocalDate from,
                                           @Param("to") LocalDate to,
                                           @Param("status") String status);

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE expense_date >= :from AND expense_date < :to AND status IN ('approved', 'paid') AND is_deleted = false", nativeQuery = true)
    BigDecimal sumAmountByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = "SELECT * FROM expenses WHERE is_recurring = true AND status = 'approved' AND is_deleted = false ORDER BY due_date", nativeQuery = true)
    List<Expense> findRecurringExpenses();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE expenses
            SET status = :status, approved_by = :approvedBy, approved_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP
            WHERE id = :expenseId AND status = 'pending'
            """, nativeQuery = true)
    int approveOrReject(@Param("expenseId") UUID expenseId,
                        @Param("status") String status,
                        @Param("approvedBy") UUID approvedBy);
}
