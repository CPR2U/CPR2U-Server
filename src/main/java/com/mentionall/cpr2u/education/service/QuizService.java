package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.OXQuiz;
import com.mentionall.cpr2u.education.domain.QuizAnswer;
import com.mentionall.cpr2u.education.domain.SelectionQuiz;
import com.mentionall.cpr2u.education.dto.quiz.*;
import com.mentionall.cpr2u.education.repository.OXQuizRepository;
import com.mentionall.cpr2u.education.repository.QuizAnswerRepository;
import com.mentionall.cpr2u.education.repository.SelectionQuizRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {

    private final OXQuizRepository oxQuizRepository;
    private final SelectionQuizRepository selectionQuizRepository;
    private final QuizAnswerRepository answerRepository;

    public void createOXQuiz(OXQuizRequestDto requestDto) {
        oxQuizRepository.save(new OXQuiz(requestDto));
    }

    @Transactional
    public void createSelectionQuiz(SelectionQuizRequestDto requestDto) {
        SelectionQuiz quiz = new SelectionQuiz(requestDto);
        selectionQuizRepository.save(quiz);

        boolean haveAnswer = false;
        for (QuizAnswerRequestDto answerDto : requestDto.getAnswerList()) {
            boolean isAnswer = (answerDto.getIndex() == requestDto.getAnswer_index());
            if (isAnswer) haveAnswer = true;

            answerRepository.save(new QuizAnswer(answerDto, quiz, isAnswer));
        }

        if (!haveAnswer) throw new CustomException(ResponseCode.BAD_REQUEST_QUIZ_WRONG_ANSWER);
    }

    @Transactional
    public List<QuizResponseDto> readRandom5Quiz() {
        List<QuizResponseDto> response = new ArrayList();
        oxQuizRepository.findRandomLimit3().forEach(q -> response.add(new OXQuizResponseDto(q)));
        selectionQuizRepository.findRandomLimit2().forEach(q -> response.add(new SelectionQuizResponseDto(q)));

        Collections.shuffle(response);
        return response;
    }
}
