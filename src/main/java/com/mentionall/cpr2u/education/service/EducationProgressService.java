package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.education.dto.EducationProgressResponseDto;
import com.mentionall.cpr2u.education.dto.ScoreRequestDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationProgressService {
    private final EducationProgressRepository progressRepository;
    private final LectureRepository lectureRepository;

    public void completeQuiz(User user, ScoreRequestDto requestDto) {
        EducationProgress progress = getEducationProgressByUser(user);

        if (progress.getLectureProgressStatus() != ProgressStatus.Completed)
            throw new CustomException(ResponseCode.BAD_REQUEST_EDUCATION_PERMISSION_DENIED);

        progress.updateQuizScore(requestDto.getScore());
        progressRepository.save(progress);

        if (requestDto.getScore() < TestStandard.quizScore)
            throw new CustomException(ResponseCode.OK_QUIZ_FAIL);
    }

    public void completePosture(User user, ScoreRequestDto requestDto) {
        EducationProgress progress = getEducationProgressByUser(user);

        if (progress.getLectureProgressStatus() != ProgressStatus.Completed ||
            progress.getQuizProgressStatus() != ProgressStatus.Completed)
            throw new CustomException(ResponseCode.BAD_REQUEST_EDUCATION_PERMISSION_DENIED);

        progress.updatePostureScore(requestDto.getScore());
        progressRepository.save(progress);

        if (progress.getPostureScore() < TestStandard.postureScore)
            throw new CustomException(ResponseCode.OK_POSTURE_FAIL);
    }

    public EducationProgressResponseDto readEducationInfo(User user) {
        EducationProgress progress = getEducationProgressByUser(user);

        return new EducationProgressResponseDto(progress, user);
    }
    public void completeLecture(User user, Long lectureId) {
        EducationProgress progress = getEducationProgressByUser(user);
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_LECTURE)
        );
        progress.updateLecture(lecture);
        progressRepository.save(progress);
    }

    private EducationProgress getEducationProgressByUser(User user) {
        return progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_EDUCATION_PROGRESS)
        );
    }

}
