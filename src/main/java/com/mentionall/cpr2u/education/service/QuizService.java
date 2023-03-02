package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
}
