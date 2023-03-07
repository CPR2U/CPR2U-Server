package com.mentionall.cpr2u.education.dto.quiz;

import com.mentionall.cpr2u.education.domain.OXQuiz;
import com.mentionall.cpr2u.education.domain.QuizType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OXQuizResponseDto extends QuizResponseDto{
    @Schema(example = "퀴즈 정답")
    private String answer;

    public OXQuizResponseDto(OXQuiz quiz) {
        this.question = quiz.getQuestion();
        this.answer = quiz.getAnswer();
        this.type = QuizType.OX;
    }
}
