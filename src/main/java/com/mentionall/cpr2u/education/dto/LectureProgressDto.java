package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LectureProgressDto {
    private int lastStep;
    private List<LectureDto> lectureList = new ArrayList();

    public LectureProgressDto(EducationProgress progress, List<LectureDto> lectureList) {
        this.lastStep = progress.getLecture().getStep();
        this.lectureList = lectureList;
    }
}
