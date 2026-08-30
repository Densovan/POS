package com.den.pos.service;

import com.den.pos.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class SoftDeletePurgeService {

    private final EntityManager entityManager;

    public SoftDeletePurgeService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public int purge(Class<? extends BaseEntity> entityClass) {
        String table = tableName(entityClass);
        return entityManager.createNativeQuery(
                        "DELETE FROM " + table + " WHERE is_deleted = true")
                .executeUpdate();
    }

    @Transactional
    public int purgeAll(Collection<Class<? extends BaseEntity>> entityClasses) {
        int total = 0;
        for (Class<? extends BaseEntity> entityClass : entityClasses) {
            total += purge(entityClass);
        }
        return total;
    }

    private String tableName(Class<? extends BaseEntity> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null && !table.name().isBlank()) {
            return table.name();
        }
        throw new IllegalArgumentException("No @Table name for " + entityClass.getSimpleName());
    }
}
