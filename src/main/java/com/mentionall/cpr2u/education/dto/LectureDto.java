package com.mentionall.cpr2u.education.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.service.LectureService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LectureDto {

    @Schema(example = "강의 섹션")
    private int step;

    @Schema(example = "강의 제목")
    private String title;

    @Schema(example = "강의 설명")
    private String description;

    @Schema(example = "강의 영상 URL")
    @JsonProperty("video_url")
    private String videoUrl;

    public LectureDto(Lecture lecture) {
        this.step = lecture.getStep();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.videoUrl = lecture.getVideoUrl();
    }
}
