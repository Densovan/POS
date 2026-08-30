package com.den.pos.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "brands")
@SoftDelete(columnName = "is_deleted")
public class Brand extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NaturalId
    @Column(name = "slug", unique = true, length = 100)
    private String slug;

    @Column(name = "logo_url", columnDefinition = "text")
    private String logoUrl;

    @Column(name = "website_url", columnDefinition = "text")
    private String websiteUrl;

    @Column(name = "is_active")
    private Boolean isActive;
}
