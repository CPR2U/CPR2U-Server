package com.mentionall.cpr2u.education.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostureRequestDto {
    @Schema(example = "자세실습 점수")
    int score;
}
