package com.mentionall.cpr2u.education.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreDto {
    @Schema(example = "점수")
    int score;

}
