package com.mentionall.cpr2u.education.dto.quiz;

import com.mentionall.cpr2u.education.domain.OXQuiz;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OXQuizRequestDto extends QuizResponseDto {
    @Schema
    private String question;

    @Schema(example = "퀴즈 정답")
    private String answer;
}
