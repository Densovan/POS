package com.den.pos.entity.system;

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

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shop_info", indexes = {
        @Index(name = "idx_shop_info_default_tax_class_id", columnList = "default_tax_class_id")
})
public class ShopInfo {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website_url", columnDefinition = "text")
    private String websiteUrl;

    @Column(name = "logo_url", columnDefinition = "text")
    private String logoUrl;

    @Column(name = "tax_id", length = 100)
    private String taxId;

    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "date_format", length = 20)
    private String dateFormat;

    @Column(name = "default_payment_method", length = 30)
    private String defaultPaymentMethod;

    @Column(name = "default_tax_class_id")
    private UUID defaultTaxClassId;

    @Column(name = "receipt_header", columnDefinition = "text")
    private String receiptHeader;

    @Column(name = "receipt_footer", columnDefinition = "text")
    private String receiptFooter;

    @Column(name = "receipt_thank_you", columnDefinition = "text")
    private String receiptThankYou;

    @Column(name = "receipt_printer_type", length = 30)
    private String receiptPrinterType;

    @Column(name = "receipt_printer_ip", length = 50)
    private String receiptPrinterIp;

    @Column(name = "receipt_printer_port")
    private Integer receiptPrinterPort;

    @Column(name = "kitchen_printer_ip", length = 50)
    private String kitchenPrinterIp;

    @Column(name = "kitchen_printer_port")
    private Integer kitchenPrinterPort;

    @Column(name = "cash_drawer_port", length = 50)
    private String cashDrawerPort;

    @Column(name = "barcode_scanner_type", length = 30)
    private String barcodeScannerType;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
