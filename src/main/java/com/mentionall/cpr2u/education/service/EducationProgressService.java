package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.*;
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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationProgressService {
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final LectureRepository lectureRepository;

    public void completeQuiz(String userId, ScoreDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_EDUCATION_PROGRESS)
        );

        // 이론 강의 수강 완료 후 퀴즈 테스트 가능
        if (progress.getLectureProgressStatus() != ProgressStatus.Completed)
            throw new CustomException(ResponseCode.BAD_REQUEST_EDUCATION_PERMISSION_DENIED);

        progress.updateQuizScore(requestDto.getScore());

        if (requestDto.getScore() < TestStandard.quizScore)
            throw new CustomException(ResponseCode.OK_QUIZ_FAIL);
    }

    public void completePosture(String userId, ScoreDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_EDUCATION_PROGRESS)
        );

        // 이론 강의 수강, 퀴즈 테스트 통과 후 자세 실습 테스트 가능
        if (progress.getLectureProgressStatus() != ProgressStatus.Completed ||
            progress.getQuizProgressStatus() != ProgressStatus.Completed)
            throw new CustomException(ResponseCode.BAD_REQUEST_EDUCATION_PERMISSION_DENIED);

        progress.updatePostureScore(requestDto.getScore());

        if (progress.getPostureScore() < TestStandard.postureScore)
            throw new CustomException(ResponseCode.OK_POSTURE_FAIL);
    }

    public EducationProgressDto readEducationInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_EDUCATION_PROGRESS)
        );

        return new EducationProgressDto(progress, user);
    }

    public void completeLecture(String userId, Long lectureId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_EDUCATION_PROGRESS)
        );
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_LECTURE)
        );

        progress.updateLecture(lecture);
    }

    public void completeAllLectureCourse(String userId) {
        List<Lecture> lectureList = lectureRepository.findAll();
        Collections.sort(lectureList);
        lectureList.forEach(lecture -> completeLecture(userId, lecture.getId()));
    }
}
