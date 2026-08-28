package com.den.pos.repository.expense;

import com.den.pos.entity.expense.ExpenseAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExpenseAttachmentRepository extends JpaRepository<ExpenseAttachment, UUID> {

    @Query(value = "SELECT * FROM expense_attachments WHERE expense_id = :expenseId ORDER BY uploaded_at DESC", nativeQuery = true)
    List<ExpenseAttachment> findByExpenseId(@Param("expenseId") UUID expenseId);
}
