package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.education.dto.quiz.SelectionQuizRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class SelectionQuiz extends Quiz {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    List<QuizAnswer> answerList = new ArrayList();

    public SelectionQuiz(SelectionQuizRequestDto requestDto) {
        this.question = requestDto.getQuestion();
    }

    public void addAnswer(QuizAnswer answer) {
        this.answerList.add(answer);
    }
}
