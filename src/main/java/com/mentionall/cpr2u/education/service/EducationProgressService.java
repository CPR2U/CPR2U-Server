package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.education.dto.EducationProgressDto;
import com.mentionall.cpr2u.education.dto.ScoreDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationProgressService {
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final LectureRepository lectureRepository;

    public void completePosture(String userId, ScoreDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );

        if (requestDto.getScore() < TestStandard.posture)
            throw new CustomException(ResponseCode.EDUCATION_POSTURE_FAIL);

        // TODO: Lecture null object 문제 처리
        if (progress.getLecture().getStep() < 4 || progress.getQuizScore() < TestStandard.quiz)
            throw new CustomException(ResponseCode.EDUCATION_PROGRESS_BAD_REQUEST);

        progress.updatePostureScore(requestDto.getScore());
    }

    public void completeQuiz(String userId, ScoreDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );

        if (requestDto.getScore() < TestStandard.quiz)
            throw new CustomException(ResponseCode.EDUCATION_QUIZ_FAIL);

        if (progress.getLecture().getStep() < 4)
            throw new CustomException(ResponseCode.EDUCATION_PROGRESS_BAD_REQUEST);

        progress.updateQuizScore(requestDto.getScore());
    }

    public EducationProgressDto readEducationInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );

        return new EducationProgressDto(progress, user);
    }

    public void completeLecture(String userId, Long lectureId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new CustomException(ResponseCode.LECTURE_NOT_FOUND)
        );

        progress.updateLecture(lecture);
    }
}
