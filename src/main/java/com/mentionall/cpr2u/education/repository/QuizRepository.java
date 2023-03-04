package com.mentionall.cpr2u.education.repository;

import com.mentionall.cpr2u.education.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM Quiz q ORDER BY RAND() LIMIT 5")
    List<Quiz> findRandomLimit5();
}
