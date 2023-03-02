package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.service.LectureService;
import lombok.Data;

@Data
public class LectureDto {
    private int step;
    private String title;
    private String description;
    private String videoUrl;

    public LectureDto(Lecture lecture) {
        this.step = lecture.getStep();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.videoUrl = lecture.getVideoUrl();
    }
}
