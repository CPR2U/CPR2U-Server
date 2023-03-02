package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.util.Timestamped;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class EducationProgress extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column
    private int quizScore;

    @Column
    private int postureScore;

    public void updateQuizScore(int score) {
        this.quizScore = score;
    }

    public void updatePostureScore(int score) {
        this.postureScore = score;
    }

    public void updateLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
