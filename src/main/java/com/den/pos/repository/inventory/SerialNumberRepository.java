package com.den.pos.repository.inventory;

import com.den.pos.entity.inventory.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SerialNumberRepository extends JpaRepository<SerialNumber, UUID> {

    @Query(value = "SELECT * FROM serial_numbers WHERE serial_number = :serialNumber AND is_deleted = false", nativeQuery = true)
    Optional<SerialNumber> findBySerialNumber(@Param("serialNumber") String serialNumber);

    @Query(value = "SELECT * FROM serial_numbers WHERE status = :status AND product_id = :productId AND is_deleted = false", nativeQuery = true)
    List<SerialNumber> findByStatusAndProductId(@Param("status") String status, @Param("productId") UUID productId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            UPDATE serial_numbers
            SET status = 'sold', sale_id = :saleId, sale_item_id = :saleItemId, customer_id = :customerId, updated_at = CURRENT_TIMESTAMP
            WHERE id = :serialNumberId AND status = 'in_stock'
            """, nativeQuery = true)
    int markAsSold(@Param("serialNumberId") UUID serialNumberId,
                   @Param("saleId") UUID saleId,
                   @Param("saleItemId") UUID saleItemId,
                   @Param("customerId") UUID customerId);
}
