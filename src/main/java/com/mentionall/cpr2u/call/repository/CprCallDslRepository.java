package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.dto.CprCallResponseDto;

import java.util.List;

public interface CprCallDslRepository {
    List<CprCallResponseDto> findAllCallInProcessByAddress(Long addressId);
}
