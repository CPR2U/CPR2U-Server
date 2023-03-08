package com.mentionall.cpr2u.education.dto.quiz;

import com.mentionall.cpr2u.education.domain.QuizAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerRequestDto  extends QuizResponseDto {
    @Schema(example = "정답 번호")
    private int index;

    @Schema(example = "후보 정답 내용")
    private String content;
}
