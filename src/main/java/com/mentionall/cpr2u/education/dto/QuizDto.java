package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.Quiz;
import lombok.Data;

@Data
public class QuizDto {
    private String question;
    private String answer;

    public QuizDto(Quiz quiz) {
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
    }
}
