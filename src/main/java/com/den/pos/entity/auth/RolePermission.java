package com.den.pos.entity.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.IdClass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(RolePermissionId.class)
@Table(name = "role_permissions", indexes = {
        @Index(name = "idx_role_permissions_permission_id", columnList = "permission_id"),
        @Index(name = "idx_role_permissions_role_id", columnList = "role_id")
})
public class RolePermission implements Persistable<RolePermissionId>, Serializable {

    @Transient
    private boolean isNew = true;

    @Id
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Id
    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;

    @Override
    public RolePermissionId getId() {
        return new RolePermissionId(roleId, permissionId);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
