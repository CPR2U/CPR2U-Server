package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.CprCall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CprCallRepository extends JpaRepository<CprCall, Long> , CprCallDslRepository, QuerydslPredicateExecutor<CprCall> {

    Page<CprCall> findAllByCaller(Pageable pageable, User caller);
}
