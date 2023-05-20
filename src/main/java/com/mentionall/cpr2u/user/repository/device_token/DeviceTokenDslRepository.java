package com.mentionall.cpr2u.user.repository.device_token;

import java.util.List;

public interface DeviceTokenDslRepository {
    List<String> findAllDeviceTokenByUserAddress(Long addressId, String userId);
}
