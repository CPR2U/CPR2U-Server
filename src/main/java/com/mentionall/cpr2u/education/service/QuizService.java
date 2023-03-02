package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.dto.QuizDto;
import com.mentionall.cpr2u.education.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public List<QuizDto> readRandomQuizList(int count) {
        List<QuizDto> response = new ArrayList();

        quizRepository.findRandom(count).stream()
                .map(q -> response.add(new QuizDto(q)));
        return response;
    }
}
