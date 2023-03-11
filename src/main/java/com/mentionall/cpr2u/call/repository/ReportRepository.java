package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
