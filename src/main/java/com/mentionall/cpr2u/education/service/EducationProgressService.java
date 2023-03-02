package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.dto.EducationProgressDto;
import com.mentionall.cpr2u.education.dto.PostureRequestDto;
import com.mentionall.cpr2u.education.dto.QuizRequestDto;
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

    public static void completePosture(String userId, PostureRequestDto requestDto) {
        // TODO: User로 EducationProgress 조회
        // User user = userRepository.findById(userId);
        // EducationProgress progress = progressRepository.findByUser(user);
        User user = new User();
        EducationProgress progress = new EducationProgress();
        progress.updatePostureScore(requestDto.getScore());
    }

    public static void completeQuiz(String userId, QuizRequestDto requestDto) {
        // TODO: User로 EducationProgress 조회
        // User user = userRepository.findById(userId);
        // EducationProgress progress = progressRepository.findByUser(user);
        User user = new User();
        EducationProgress progress = new EducationProgress();
        progress.updateQuizScore(requestDto.getScore());
    }

    public EducationProgressDto readEducationInfo(String userId) {
        // TODO: User로 EducationProgress 조회
        // User user = userRepository.findById(userId);
        // EducationProgress progress = progressRepository.findByUser(user);
        User user = new User();
        EducationProgress progress = new EducationProgress();

        return new EducationProgressDto(progress, user);
    }

    public void completeLecture(String userId, Long lectureId) {
        // TODO: User로 EducationProgress 조회
        // User user = userRepository.findById(userId);
        // EducationProgress progress = progressRepository.findByUser(user);
        User user = new User();
        EducationProgress progress = new EducationProgress();
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(
                () -> new CustomException(ResponseCode.LECTURE_NOT_FOUND)
        );

        progress.updateLecture(lecture);
    }
}
