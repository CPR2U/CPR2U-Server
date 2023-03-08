package com.mentionall.cpr2u.education.dto.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.education.domain.QuizAnswer;
import com.mentionall.cpr2u.education.domain.QuizType;
import com.mentionall.cpr2u.education.domain.SelectionQuiz;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SelectionQuizResponseDto extends QuizResponseDto {

    @Schema(example = "퀴즈 정답 ID")
    private Long answer;

    @Schema(example = "정답 후보 리스트")
    @JsonProperty(value = "answer_list")
    List<QuizAnswerResponseDto> answerList = new ArrayList<>();

    public SelectionQuizResponseDto(SelectionQuiz quiz) {
        this.question = quiz.getQuestion();
        this.type = QuizType.SELECTION;

        for (int i = 0; i < quiz.getAnswerList().size(); i++) {
            QuizAnswer answer = quiz.getAnswerList().get(i);
            if (answer.isAnswer()) this.answer = answer.getId();
            answerList.add(new QuizAnswerResponseDto(answer));
        }
    }
}
