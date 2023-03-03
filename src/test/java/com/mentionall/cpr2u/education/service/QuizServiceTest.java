package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.dto.QuizDto;
import com.mentionall.cpr2u.education.repository.QuizRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class QuizServiceTest {
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    @AfterEach
    private void afterEach() {
        quizRepository.deleteAll();
    }

    @Test
    public void readRandom5Quiz() {
        //given
        quizService.createQuiz(new QuizDto("지구는 둥글다.", "O"));
        quizService.createQuiz(new QuizDto("고양이는 귀엽다.", "O"));
        quizService.createQuiz(new QuizDto("숙명여대는 일본에 있다.", "X"));
        quizService.createQuiz(new QuizDto("배고프다.", "X"));
        quizService.createQuiz(new QuizDto("질문은 총 7개이다.", "O"));
        quizService.createQuiz(new QuizDto("CPR2U는 4글자이다.", "X"));
        quizService.createQuiz(new QuizDto("이것은 임시 질문들이다.", "O"));

        //when
        List<QuizDto> quizDtoList = quizService.readRandom5Quiz();

        //then
        assertThat(quizDtoList.size()).isEqualTo(5);

        Set<String> questionSet = new HashSet();
        for (QuizDto quiz : quizDtoList) {
            questionSet.add(quiz.getQuestion());
        }
        assertThat(questionSet.size()).isEqualTo(5);

    }
}
