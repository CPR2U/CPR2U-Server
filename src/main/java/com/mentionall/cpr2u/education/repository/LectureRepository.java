package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Boolean existsByStep(int step);
}
