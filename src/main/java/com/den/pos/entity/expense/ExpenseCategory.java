package com.den.pos.entity.expense;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "expense_categories")
@SoftDelete(columnName = "is_deleted")
public class ExpenseCategory extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;
}
