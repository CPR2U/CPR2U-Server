package com.mentionall.cpr2u.education.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EducationProgressDto {

    @Schema(example = "사용자의 엔젤 상태")
    @JsonProperty("angel_status")
    private AngelStatusEnum angelStatus;

    @Schema(example = "사용자의 총 학습 완수율(0.0 ~ 1.0)")
    @JsonProperty("progress_all")
    private double totalProgress;

    @Schema(example = "사용자의 완강 여부")
    @JsonProperty("is_lecture_completed")
    private ProgressStatus isLectureCompleted;

    @Schema(example = "마지막으로 이수를 완료한 강의명")
    @JsonProperty("last_lecture_title")
    private String lastLectureTitle;

    @Schema(example = "사용자의 퀴즈 완료 여부")
    @JsonProperty("is_quiz_completed")
    private ProgressStatus isQuizCompleted;

    @Schema(example = "사용자의 자세 실습 완료 여부")
    @JsonProperty("is_posture_completed")
    private ProgressStatus isPostureCompleted;

    public EducationProgressDto(EducationProgress progress, User user) {
        this.angelStatus = user.getStatus();
        this.totalProgress = progress.getTotalProgress();

        this.lastLectureTitle = progress.getLastLecture().getTitle();
        this.isLectureCompleted = progress.getLectureProgressStatus();
        this.isQuizCompleted = progress.getQuizProgressStatus();
        this.isPostureCompleted = progress.getPostureProgressStatus();
    }
}
