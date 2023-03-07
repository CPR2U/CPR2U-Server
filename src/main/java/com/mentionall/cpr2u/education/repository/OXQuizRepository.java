package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.OXQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OXQuizRepository extends JpaRepository<OXQuiz, Long> {
    // TODO: Querydsl로 OX, SelectionQuiz 조인 -> 랜덤으로 퀴즈 조회
    @Query(nativeQuery = true, value = "SELECT * FROM OXQuiz q ORDER BY RAND() LIMIT 3")
    List<OXQuiz> findRandomLimit3();
}
