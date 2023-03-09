package com.mentionall.cpr2u.user.repository;

import com.mentionall.cpr2u.user.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String> {
    Optional<DeviceToken> findByUserId(String userId);
}
