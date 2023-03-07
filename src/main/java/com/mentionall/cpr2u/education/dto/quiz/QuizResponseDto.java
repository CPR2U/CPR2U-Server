package com.mentionall.cpr2u.education.dto.quiz;

import com.mentionall.cpr2u.education.domain.QuizType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Getter
public abstract class QuizResponseDto {
    @Schema(example = "퀴즈 질문")
    protected String question;

    @Schema(example = "퀴즈 타입")
    @Enumerated(EnumType.STRING)
    protected QuizType type;
}
