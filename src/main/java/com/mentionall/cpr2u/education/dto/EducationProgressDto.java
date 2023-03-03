package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EducationProgressDto {

    @Schema(example = "사용자의 엔젤 상태")
    private AngelStatusEnum angelStatus;

    @Schema(example = "사용자의 총 학습 완수율")
    private double progressAll;

    @Schema(example = "사용자의 완강 여부")
    private ProgressStatus isLectureCompleted;

    @Schema(example = "마지막으로 이수를 완료한 강의명")
    private String lastLectureTitle;

    @Schema(example = "사용자의 퀴즈 완료 여부")
    private ProgressStatus isQuizCompleted;

    @Schema(example = "사용자의 자세 실습 완료 여부")
    private ProgressStatus isPostureCompleted;


    public EducationProgressDto(EducationProgress progress, User user) {
        this.angelStatus = user.getStatus();

        int currentProgress = progress.getLecture().getStep();
        if (progress.getQuizScore() >= TestStandard.quiz) currentProgress++;
        if (progress.getPostureScore() >= TestStandard.posture) currentProgress++;

        progressAll = (double)currentProgress / 6.0;
        lastLectureTitle = progress.getLecture().getTitle();

        isLectureCompleted = ProgressStatus.NotCompleted;
        isQuizCompleted = ProgressStatus.NotCompleted;
        isPostureCompleted = ProgressStatus.NotCompleted;

        switch(currentProgress) {
            case 6:
                isPostureCompleted = ProgressStatus.Completed;
            case 5:
                isQuizCompleted = ProgressStatus.Completed;
            case 4:
                isLectureCompleted = ProgressStatus.Completed;
                break;
            case 3:
            case 2:
            case 1:
                isLectureCompleted = ProgressStatus.InProgress;
            default:
                break;
        }
    }
}
