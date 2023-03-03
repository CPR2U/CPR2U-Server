package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.Quiz;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QuizDto {

    @Schema(example = "퀴즈 질문")
    private String question;

    @Schema(example = "퀴즈 정답")
    private String answer;

    public QuizDto(Quiz quiz) {
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
    }
}
