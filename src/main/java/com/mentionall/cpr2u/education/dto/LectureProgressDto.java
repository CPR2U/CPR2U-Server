package com.mentionall.cpr2u.education.dto;

import lombok.Data;

import java.util.List;

@Data
public class LectureProgressDto {
    private int lastStep;
    private List<LectureDto> lectureList;
}
