package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.education.dto.QuizDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String question;

    @Column(length = 1)
    private String answer;

    public Quiz(QuizDto requestDto) {
        this.question = requestDto.getQuestion();
        this.answer = requestDto.getAnswer();
    }

    public void update(QuizDto requestDto) {
        this.question = requestDto.getQuestion();
        this.answer = requestDto.getAnswer();
    }
}
