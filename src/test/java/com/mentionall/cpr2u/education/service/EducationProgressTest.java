package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.education.dto.EducationProgressDto;
import com.mentionall.cpr2u.education.dto.LectureRequestDto;
import com.mentionall.cpr2u.education.dto.LectureResponseDto;
import com.mentionall.cpr2u.education.dto.ScoreDto;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.service.UserService;
import com.mentionall.cpr2u.util.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EducationProgressTest {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private EducationProgressService progressService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LectureRepository lectureRepository;

    @BeforeEach
    public void createLectures() {
        lectureRepository.deleteAll();
        lectureService.createLecture(new LectureRequestDto(1, "강의1", "1입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(2, "강의2", "2입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(3, "강의3", "3입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(4, "강의4", "4입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(5, "강의5", "자세 강의입니다.", "https://naver.com", "POSTURE"));
    }

    @Test
    @Transactional
    public void completeLecture() {
        //given
        String userId = getUserId("현애", "010-0000-0000");

        //when & then
        assertThatLectureCourseIsNotStarted(userId);

        for (LectureResponseDto lecture : lectureService.readAllTheoryLecture()) {
            progressService.completeLecture(userId, lecture.getId());

            int currentStep = lectureService.readLectureProgress(userId).getCurrentStep();
            assertThat(currentStep).isEqualTo(lecture.getStep());
            if (currentStep < TestStandard.finalLectureStep)
                assertThatLectureCourseIsInProgress(userId, lecture);
        }
        assertThatLectureCourseIsCompleted(userId);
    }

    @Test
    @Transactional
    public void completeQuiz() {
        //given
        String userId = getUserId("현애", "010-0000-0000");
        progressService.completeLectureCourse(userId);

        //when quiz test is not started,
        ProgressStatus quizStatus = progressService.readEducationInfo(userId).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(ProgressStatus.NotCompleted);

        //when the user fails the quiz test,
        Assertions.assertThrows(CustomException.class, () -> {
            progressService.completeQuiz(userId, new ScoreDto(50));
        });
        quizStatus = progressService.readEducationInfo(userId).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(ProgressStatus.NotCompleted);

        //when the user succeeds the quiz test,
        progressService.completeQuiz(userId, new ScoreDto(100));
        quizStatus = progressService.readEducationInfo(userId).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(ProgressStatus.Completed);
    }

    @Test
    @Transactional
    public void completePosture() {
        //given
        String userId = getUserId("현애", "010-0000-0000");
        progressService.completeLectureCourse(userId);
        progressService.completeQuiz(userId, new ScoreDto(100));

        //when a posture test is not started,
        EducationProgressDto educationProgress = progressService.readEducationInfo(userId);
        assertThat(educationProgress.getIsPostureCompleted()).isEqualTo(ProgressStatus.NotCompleted);

        //when the user fails the posture test,
        Assertions.assertThrows(CustomException.class, () -> {
            progressService.completePosture(userId, new ScoreDto(79));
        });
        educationProgress = progressService.readEducationInfo(userId);
        assertThat(educationProgress.getIsPostureCompleted()).isEqualTo(ProgressStatus.NotCompleted);

        //when the user succeeds the posture test,
        progressService.completePosture(userId, new ScoreDto(81));
        educationProgress = progressService.readEducationInfo(userId);
        assertThat(educationProgress.getTotalProgress()).isEqualTo(1.0);
        assertThat(educationProgress.getIsPostureCompleted()).isEqualTo(ProgressStatus.Completed);
    }

    public void completeQuizWithoutLecture() {
        // TODO: 강의 수강 여부가 NOT COMPLETED / IN PROGRESS인 경우
    }

    public void completePostureWithoutQuizOrLecture() {
        // TODO: 강의 수강 여부가 NOT COMPLETED / IN PROGRESS / COMPLETED인 경우
    }

    private String getUserId(String nickname, String phoneNumber) {
        String accessToken = userService.signup(new UserSignUpDto(nickname, phoneNumber)).getAccessToken();
        return jwtTokenProvider.getUserId(accessToken);
    }

    private void assertThatLectureCourseIsNotStarted(String userId) {
        assertThatLectureProgressIsEqualTo(userId, ProgressStatus.NotCompleted, 0.0, "");
    }

    private void assertThatLectureCourseIsInProgress(String userId, LectureResponseDto lecture) {
        assertThatLectureProgressIsEqualTo(userId, ProgressStatus.InProgress,
                (double)lecture.getStep() / (double)TestStandard.totalStep, lecture.getTitle());
    }

    private void assertThatLectureCourseIsCompleted(String userId) {
        assertThatLectureProgressIsEqualTo(userId, ProgressStatus.Completed,
                (double)TestStandard.finalLectureStep / (double)TestStandard.totalStep, "");
    }

    private void assertThatLectureProgressIsEqualTo(String userId, ProgressStatus status, double totalProgress, String lectureTitle) {
        EducationProgressDto progress = progressService.readEducationInfo(userId);
        assertThat(progress.getIsLectureCompleted()).isEqualTo(status);
        assertThat(progress.getTotalProgress()).isEqualTo(totalProgress);
        if (status != ProgressStatus.Completed) assertThat(progress.getLastLectureTitle()).isEqualTo(lectureTitle);
    }
}
