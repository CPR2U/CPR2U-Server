package com.mentionall.cpr2u.education.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QuizRequestDto {
    @Schema(example = "퀴즈 점수")
    int score;
}
