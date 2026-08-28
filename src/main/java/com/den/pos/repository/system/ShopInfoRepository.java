package com.den.pos.repository.system;

import com.den.pos.entity.system.ShopInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ShopInfoRepository extends JpaRepository<ShopInfo, UUID> {

    @Query(value = "SELECT * FROM shop_info ORDER BY updated_at DESC LIMIT 1", nativeQuery = true)
    Optional<ShopInfo> findCurrentShopInfo();
}
