package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.Quiz;
import com.mentionall.cpr2u.education.dto.QuizDto;
import com.mentionall.cpr2u.education.repository.QuizRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public void createQuiz(QuizDto requestDto) {
        Quiz quiz = new Quiz(requestDto);
        quizRepository.save(quiz);
    }

    public void updateQuiz(Long quizId, QuizDto requestDto) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new CustomException(ResponseCode.QUIZ_NOT_FOUND)
        );

        quiz.update(requestDto);
        quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new CustomException(ResponseCode.QUIZ_NOT_FOUND)
        );

        quizRepository.delete(quiz);
    }

    public List<QuizDto> readRandom5Quiz() {
        return quizRepository.findRandomLimit5().stream()
                .map(q -> new QuizDto(q))
                .collect(Collectors.toList());
    }
}
