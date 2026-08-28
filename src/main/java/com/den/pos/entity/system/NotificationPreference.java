package com.den.pos.entity.system;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @NaturalId
    @Column(name = "user_id", unique = true)
    private UUID userId;

    @Column(name = "email_low_stock")
    private Boolean emailLowStock;

    @Column(name = "email_out_of_stock")
    private Boolean emailOutOfStock;

    @Column(name = "email_expiring_products")
    private Boolean emailExpiringProducts;

    @Column(name = "email_daily_summary")
    private Boolean emailDailySummary;

    @Column(name = "email_new_purchase")
    private Boolean emailNewPurchase;

    @Column(name = "email_new_sale")
    private Boolean emailNewSale;

    @Column(name = "in_app_low_stock")
    private Boolean inAppLowStock;

    @Column(name = "in_app_out_of_stock")
    private Boolean inAppOutOfStock;

    @Column(name = "in_app_expiring_products")
    private Boolean inAppExpiringProducts;

    @Column(name = "in_app_daily_summary")
    private Boolean inAppDailySummary;

    @Column(name = "sms_low_stock")
    private Boolean smsLowStock;

    @Column(name = "sms_out_of_stock")
    private Boolean smsOutOfStock;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
