package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.Dispatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface DispatchRepository extends JpaRepository<Dispatch, Long> {
    List<Dispatch> findAllByCprCallId(Long cprCallId);
    Page<Dispatch> findAllByDispatcher(Pageable pageable, User dispatcher);
}
