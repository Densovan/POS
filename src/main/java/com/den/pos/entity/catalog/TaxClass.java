package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tax_classes")
@SoftDelete(columnName = "is_deleted")
public class TaxClass extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "is_default")
    private Boolean isDefault;
}
