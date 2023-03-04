package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.domain.LectureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Boolean existsByStep(int step);

    List<Lecture> findAllByType(LectureType type);
}
