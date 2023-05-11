package com.mentionall.cpr2u.education.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("퀴즈 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QuizServiceTest {
    @Autowired
    private QuizService quizService;

    @Test
    public void 다섯개의_퀴즈를_무작위_조회한_경우() {
        //when
        var quizList = quizService.readRandom5Quiz();

        //then
        assertThat(quizList.size()).isEqualTo(5);
    }

    //TODO
    @Test
    public void 퀴즈_생성() {

    }
}
