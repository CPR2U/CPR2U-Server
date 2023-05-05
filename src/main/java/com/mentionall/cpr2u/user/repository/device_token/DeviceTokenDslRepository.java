package com.mentionall.cpr2u.user.repository.device_token;

import com.mentionall.cpr2u.user.domain.DeviceToken;

import java.util.List;

public interface DeviceTokenDslRepository {
    List<DeviceToken> findAllDeviceTokenByUserAddress(Long addressId, String userId);
}
