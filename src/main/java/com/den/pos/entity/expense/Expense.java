package com.den.pos.entity.expense;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "expenses", indexes = {
        @Index(name = "idx_expenses_approved_by", columnList = "approved_by"),
        @Index(name = "idx_expenses_category_id", columnList = "category_id"),
        @Index(name = "idx_expenses_created_by", columnList = "created_by")
})
@SoftDelete(columnName = "is_deleted")
public class Expense extends BaseEntity {

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "recurrence_pattern", length = 20)
    private String recurrencePattern;

    @Column(name = "recurrence_end_date")
    private LocalDate recurrenceEndDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
}
