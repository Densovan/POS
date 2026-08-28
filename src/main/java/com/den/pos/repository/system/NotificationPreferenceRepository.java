package com.den.pos.repository.system;

import com.den.pos.entity.system.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID> {

    @Query(value = "SELECT * FROM notification_preferences WHERE user_id = :userId", nativeQuery = true)
    Optional<NotificationPreference> findByUserId(@Param("userId") UUID userId);
}
