package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.CprCallStatus;
import com.mentionall.cpr2u.call.dto.CprCallDto;

import java.util.List;

public interface CprCallDslRepository {
    List<CprCallDto> findAllByStatusAndAddress(CprCallStatus cprCallStatus, Long addressId);
}
