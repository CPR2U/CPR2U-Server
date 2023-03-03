package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LectureProgressDto {
    @Schema(example = "마지막으로 이수 완료한 강의 섹션")
    private int lastStep;

    private List<LectureDto> lectureList = new ArrayList();

    public LectureProgressDto(EducationProgress progress, List<LectureDto> lectureList) {
        this.lastStep = progress.getLecture().getStep();
        this.lectureList = lectureList;
    }
}
