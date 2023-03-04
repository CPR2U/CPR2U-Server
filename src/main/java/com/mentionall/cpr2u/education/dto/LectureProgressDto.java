package com.mentionall.cpr2u.education.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LectureProgressDto {
    @Schema(example = "마지막으로 이수 완료한 강의 섹션")

    @JsonProperty("last_step")
    private int lastStep;

    @JsonProperty("lecture_list")
    private List<LectureResponseDto> lectureList = new ArrayList();

    public LectureProgressDto(EducationProgress progress, List<LectureResponseDto> lectureList) {
        this.lastStep = (progress.getLecture() == null) ? 0 : progress.getLecture().getStep();
        this.lectureList = lectureList;
    }
}
