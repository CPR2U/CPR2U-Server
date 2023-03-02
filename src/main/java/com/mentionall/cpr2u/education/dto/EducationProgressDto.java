package com.mentionall.cpr2u.education.dto;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.domain.TestStandard;
import lombok.Data;

@Data
public class EducationProgressDto {

    // TODO: user angel status

    private double progressAll;
    private ProgressStatus isLectureCompleted;
    private String lastLectureTitle;
    private ProgressStatus isQuizCompleted;
    private ProgressStatus isPostureCompleted;


    public EducationProgressDto(EducationProgress progress) {
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
