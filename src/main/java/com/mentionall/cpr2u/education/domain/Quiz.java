package com.mentionall.cpr2u.education.domain;

import javax.persistence.*;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String question;

    @Column(length = 1)
    private String answer;
}
