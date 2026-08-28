package com.den.pos.repository.customer;

import com.den.pos.entity.customer.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, UUID> {

    @Query(value = "SELECT * FROM customer_addresses WHERE customer_id = :customerId ORDER BY is_default DESC, created_at DESC", nativeQuery = true)
    List<CustomerAddress> findByCustomerId(@Param("customerId") UUID customerId);

    @Query(value = "SELECT * FROM customer_addresses WHERE customer_id = :customerId AND is_default = true LIMIT 1", nativeQuery = true)
    Optional<CustomerAddress> findDefaultByCustomerId(@Param("customerId") UUID customerId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE customer_addresses SET is_default = false WHERE customer_id = :customerId", nativeQuery = true)
    int clearDefaultForCustomer(@Param("customerId") UUID customerId);
}
