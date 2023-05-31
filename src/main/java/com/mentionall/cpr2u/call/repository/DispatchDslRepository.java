package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.Dispatch;

import java.util.List;

public interface DispatchDslRepository {

    boolean existsByCprCallIdAndUserId(Long cprCallId, String userId);
}
