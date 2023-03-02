package com.mentionall.cpr2u.education.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(length = 255)
    private String videoUrl;

    @Column
    private int step;

    @Column(length = 50)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecture")
    List<EducationProgress> progressList = new ArrayList();
}
