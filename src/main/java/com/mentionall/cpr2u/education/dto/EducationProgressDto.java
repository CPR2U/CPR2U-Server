package com.mentionall.cpr2u.education.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EducationProgressDto {
    @Schema(example = "사용자의 엔젤 상태(0: 수료 / 1: 만료 / 2: 미수료)")
    @JsonProperty("angel_status")
    private int angelStatus;

    @Schema(example = "사용자의 총 학습 완수율(0.0 ~ 1.0 사이 Double 값)")
    @JsonProperty("progress_percent")
    private double progressPercent;

    @Schema(example = "사용자의 완강 여부(Not Completed / Completed)")
    @JsonProperty("is_lecture_completed")
    private ProgressStatus isLectureCompleted;

    @Schema(example = "마지막으로 이수를 완료한 강의명(무시해주세요.)")
    @JsonProperty("last_lecture_title")
    private String lastLectureTitle;

    @Schema(example = "사용자의 퀴즈 완료 여부(Not Completed / Completed)")
    @JsonProperty("is_quiz_completed")
    private ProgressStatus isQuizCompleted;

    @Schema(example = "사용자의 자세 실습 완료 여부(Not Completed / Completed)")
    @JsonProperty("is_posture_completed")
    private ProgressStatus isPostureCompleted;

    public EducationProgressDto(EducationProgress progress, User user) {
        this.angelStatus = user.getStatus().ordinal();
        this.progressPercent = progress.getTotalProgress();

        this.lastLectureTitle = progress.getLastLecture().getTitle();
        this.isLectureCompleted = progress.getLectureProgressStatus();
        this.isQuizCompleted = progress.getQuizProgressStatus();
        this.isPostureCompleted = progress.getPostureProgressStatus();
    }
}
