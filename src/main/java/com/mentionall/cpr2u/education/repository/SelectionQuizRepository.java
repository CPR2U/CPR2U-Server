package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.SelectionQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionQuizRepository extends JpaRepository<SelectionQuiz, Long> {
    // TODO: Querydsl로 OX, SelectionQuiz 조인 -> 랜덤으로 퀴즈 조회
    @Query(nativeQuery = true, value = "SELECT * FROM selection_quiz q ORDER BY RAND() LIMIT 2")
    List<SelectionQuiz> findRandomLimit2();
}
