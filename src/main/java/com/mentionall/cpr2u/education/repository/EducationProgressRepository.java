package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationProgressRepository extends JpaRepository<EducationProgress, Long> {
}
