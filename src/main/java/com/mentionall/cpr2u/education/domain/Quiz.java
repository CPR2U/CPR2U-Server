package com.mentionall.cpr2u.education.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public abstract class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(length = 50)
    protected String question;
}
