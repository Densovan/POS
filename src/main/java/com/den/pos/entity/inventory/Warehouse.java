package com.den.pos.entity.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;
import com.den.pos.entity.BaseEntity;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "warehouses", indexes = {
        @Index(name = "idx_warehouses_manager_id", columnList = "manager_id")
})
@SoftDelete(columnName = "is_deleted")
public class Warehouse extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NaturalId
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "is_active")
    private Boolean isActive;
}
