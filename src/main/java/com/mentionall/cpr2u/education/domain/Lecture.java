package com.mentionall.cpr2u.education.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(length = 255)
    private String videoUrl;

    @Column
    private int sequence;

    @Column(length = 50)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecture")
    List<EducationProgress> progressList = new ArrayList();
}
