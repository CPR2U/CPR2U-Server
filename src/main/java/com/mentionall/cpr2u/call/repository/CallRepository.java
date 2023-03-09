package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call, Long> {
}
