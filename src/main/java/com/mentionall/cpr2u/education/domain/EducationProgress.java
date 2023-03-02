package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.util.Timestamped;
import javax.persistence.*;

@Entity
public class EducationProgress extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: User Entity PK
    @Column(length = 20)
    private String user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column
    private int quizScore;

    @Column
    private int postureScore;
}
