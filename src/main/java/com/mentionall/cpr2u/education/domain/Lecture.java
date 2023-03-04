package com.mentionall.cpr2u.education.domain;

import com.mentionall.cpr2u.education.dto.LectureRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Lecture implements Comparable<Lecture> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String title;

    @Column(length = 255)
    private String videoUrl;

    @Column(unique = true)
    private int step;

    @Column(length = 50)
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private LectureType type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lecture")
    List<EducationProgress> progressList = new ArrayList();

    public Lecture(LectureRequestDto requestDto) {
        this.step = requestDto.getStep();
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.videoUrl = requestDto.getVideoUrl();
        this.type = LectureType.valueOf(requestDto.getType());
    }

    @Override
    public int compareTo(Lecture other) {
        return this.step - other.step;
    }
}
