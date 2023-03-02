package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.Lecture;
import lombok.Data;

@Data
public class PostureLectureDto {
    private String title;
    private String description;
    private String videoUrl;

    public PostureLectureDto(Lecture lecture) {
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.videoUrl = lecture.getVideoUrl();
    }
}
