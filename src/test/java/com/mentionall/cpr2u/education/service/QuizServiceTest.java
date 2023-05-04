package com.mentionall.cpr2u.education.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class QuizServiceTest {
    @Autowired
    private QuizService quizService;

    @Test
    @DisplayName("5개의 퀴즈를 무작위 조회")
    public void readRandom5Quiz() {
        //when
        var quizList = quizService.readRandom5Quiz();

        //then
        assertThat(quizList.size()).isEqualTo(5);
    }
}
