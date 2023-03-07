package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.education.dto.quiz.OXQuizRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class OXQuiz extends Quiz{

    @Column(length = 1)
    private String answer;

    public OXQuiz(OXQuizRequestDto requestDto) {
        this.question = requestDto.getQuestion();
        this.answer = requestDto.getAnswer();
    }
}
