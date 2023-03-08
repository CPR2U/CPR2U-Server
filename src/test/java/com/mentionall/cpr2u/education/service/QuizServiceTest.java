package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.QuizType;
import com.mentionall.cpr2u.education.dto.quiz.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class QuizServiceTest {
    @Autowired
    private QuizService quizService;

    @Test
    @Transactional
    public void readRandom5Quiz() {
        //given
        create7OXQuiz();
        create2SelectionQuiz();

        //when & then
        int ox = 0;
        int selection = 0;

        for (QuizResponseDto quiz : quizService.readRandom5Quiz()) {
            if (quiz.getType() == QuizType.OX) {
                ox++;
            } else {
                selection++;
            }
        }

        // TODO: 매직 넘버 치환
        assertThat(ox).isEqualTo(3);
        assertThat(selection).isEqualTo(2);
    }

    private void create2SelectionQuiz() {
        List<QuizAnswerRequestDto> answerList = new ArrayList();
        answerList.add(new QuizAnswerRequestDto(1, "한국"));
        answerList.add(new QuizAnswerRequestDto(2, "미국"));
        answerList.add(new QuizAnswerRequestDto(3, "일본"));
        answerList.add(new QuizAnswerRequestDto(4, "호주"));
        quizService.createSelectionQuiz(new SelectionQuizRequestDto("여기는 어디?", 1, answerList));
        ;

        answerList = new ArrayList();
        answerList.add(new QuizAnswerRequestDto(1, "Corea"));
        answerList.add(new QuizAnswerRequestDto(2, "Korea"));
        answerList.add(new QuizAnswerRequestDto(3, "KKorea"));
        answerList.add(new QuizAnswerRequestDto(4, "CCorea"));
        quizService.createSelectionQuiz(new SelectionQuizRequestDto("한국은 영어로?", 2, answerList));
        ;
    }

    private void create7OXQuiz() {
        quizService.createOXQuiz(new OXQuizRequestDto("지구는 둥글다.", "O"));
        quizService.createOXQuiz(new OXQuizRequestDto("고양이는 귀엽다.", "O"));
        quizService.createOXQuiz(new OXQuizRequestDto("숙명여대는 일본에 있다.", "X"));
        quizService.createOXQuiz(new OXQuizRequestDto("배고프다.", "X"));
        quizService.createOXQuiz(new OXQuizRequestDto("질문은 총 7개이다.", "O"));
        quizService.createOXQuiz(new OXQuizRequestDto("CPR2U는 4글자이다.", "X"));
        quizService.createOXQuiz(new OXQuizRequestDto("이것은 임시 질문들이다.", "O"));
    }
}
