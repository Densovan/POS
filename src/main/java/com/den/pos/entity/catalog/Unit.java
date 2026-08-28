package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "units", indexes = {
        @Index(name = "idx_units_base_unit_id", columnList = "base_unit_id")
})
public class Unit {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "abbreviation", length = 10)
    private String abbreviation;

    @Column(name = "is_base_unit")
    private Boolean isBaseUnit;

    @Column(name = "conversion_factor", precision = 10, scale = 4)
    private BigDecimal conversionFactor;

    @Column(name = "base_unit_id")
    private UUID baseUnitId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
