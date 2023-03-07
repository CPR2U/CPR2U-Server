package com.mentionall.cpr2u.education.dto.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectionQuizRequestDto {

    @Schema(example = "퀴즈 질문")
    private String question;

    @Schema(example = "퀴즈 정답 번호")
    private int answer_index;

    @Schema(example = "정답 후보 리스트")
    @JsonProperty(value = "answer_list")
    List<QuizAnswerRequestDto> answerList;
}
