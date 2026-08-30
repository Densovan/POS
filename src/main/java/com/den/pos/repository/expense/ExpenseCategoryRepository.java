package com.den.pos.repository.expense;

import com.den.pos.entity.expense.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {

    @Query(value = "SELECT * FROM expense_categories WHERE is_active = true AND is_deleted = false ORDER BY name", nativeQuery = true)
    List<ExpenseCategory> findAllActive();
}
