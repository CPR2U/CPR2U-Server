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

        //when lecture course is not started,
        assertThatLectureProgressIsEqualTo(userId, null, ProgressStatus.NotCompleted);

        //when lecture course is in progress,
        for (LectureResponseDto lecture : lectureService.readAllTheoryLecture()) {
            progressService.completeLecture(userId, lecture.getId());

            int currentStep = lectureService.readLectureProgress(userId).getCurrentStep();
            assertThat(currentStep).isEqualTo(lecture.getStep());
            if (currentStep < TestStandard.finalLectureStep)
                assertThatLectureProgressIsEqualTo(userId, lecture, ProgressStatus.InProgress);
        }

        //when lecture course is completed,
        assertThatLectureProgressIsEqualTo(userId, null, ProgressStatus.Completed);
    }

    @Test
    @Transactional
    public void completeQuiz() {
        //given
        String userId = getUserId("현애", "010-0000-0000");
        progressService.completeAllLectureCourse(userId);

        //when quiz test is not started,
        assertThatQuizProgressIsEqualTo(userId, ProgressStatus.NotCompleted);

        //when the user fails the quiz test,
        Assertions.assertThrows(CustomException.class, () -> progressService.completeQuiz(userId, new ScoreDto(50)));
        assertThatQuizProgressIsEqualTo(userId, ProgressStatus.NotCompleted);

        //when the user succeeds the quiz test,
        progressService.completeQuiz(userId, new ScoreDto(100));
        assertThatQuizProgressIsEqualTo(userId, ProgressStatus.Completed);
    }

    @Test
    @Transactional
    public void completePosture() {
        //given
        String userId = getUserId("현애", "010-0000-0000");
        progressService.completeAllLectureCourse(userId);
        progressService.completeQuiz(userId, new ScoreDto(100));

        //when a posture test is not started,
        assertThatPostureProgressIsEqualTo(userId, ProgressStatus.NotCompleted);

        //when the user fails the posture test,
        Assertions.assertThrows(CustomException.class, () -> progressService.completePosture(userId, new ScoreDto(79)));
        assertThatPostureProgressIsEqualTo(userId, ProgressStatus.NotCompleted);

        //when the user succeeds the posture test,
        progressService.completePosture(userId, new ScoreDto(81));
        assertThatPostureProgressIsEqualTo(userId, ProgressStatus.Completed);
    }

    @Test
    @Transactional
    public void completeQuizWithoutLecture() {
        //given
        String userId = getUserId("현애", "010-0000-0000");

        // when the lecture course is not completed,
        Assertions.assertThrows(CustomException.class, () -> progressService.completeQuiz(userId, new ScoreDto(100)));

        // when the lecture course is in progress,
        LectureResponseDto lecture = lectureService.readAllTheoryLecture().get(0);
        progressService.completeLecture(userId, lecture.getId());
        Assertions.assertThrows(CustomException.class, () -> progressService.completeQuiz(userId, new ScoreDto(100)));
    }

    @Test
    @Transactional
    public void completePostureWithoutQuizOrLecture() {
        //given
        String userId = getUserId("현애", "010-0000-0000");

        // when the lecture course is not completed,
        Assertions.assertThrows(CustomException.class, () -> progressService.completePosture(userId, new ScoreDto(100)));

        // when the lecture course is in progress,
        LectureResponseDto lecture = lectureService.readAllTheoryLecture().get(0);
        progressService.completeLecture(userId, lecture.getId());
        Assertions.assertThrows(CustomException.class, () -> progressService.completePosture(userId, new ScoreDto(100)));

        // when the lecture course is completed, but quiz test is not
        progressService.completeAllLectureCourse(userId);
        Assertions.assertThrows(CustomException.class, () -> progressService.completePosture(userId, new ScoreDto(100)));
    }

    private String getUserId(String nickname, String phoneNumber) {
        String accessToken = userService.signup(new UserSignUpDto(nickname, phoneNumber)).getAccessToken();
        return jwtTokenProvider.getUserId(accessToken);
    }

    private void assertThatLectureProgressIsEqualTo(String userId, LectureResponseDto lecture, ProgressStatus status) {
        EducationProgressDto progress = progressService.readEducationInfo(userId);
        assertThat(progress.getIsLectureCompleted()).isEqualTo(status);

        double totalProgress =
                (status == ProgressStatus.Completed) ? ((double)TestStandard.finalLectureStep / (double)TestStandard.totalStep) :
                (status == ProgressStatus.InProgress) ? (double)lecture.getStep() / (double)TestStandard.totalStep : 0.0;
        assertThat(progress.getTotalProgress()).isEqualTo(totalProgress);

        if (status == ProgressStatus.InProgress)
            assertThat(progress.getLastLectureTitle()).isEqualTo(lecture.getTitle());
    }

    private void assertThatQuizProgressIsEqualTo(String userId, ProgressStatus status) {
        ProgressStatus quizStatus = progressService.readEducationInfo(userId).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(status);
    }

    private void assertThatPostureProgressIsEqualTo(String userId, ProgressStatus status) {
        EducationProgressDto progress = progressService.readEducationInfo(userId);
        ProgressStatus postureStatus = progress.getIsPostureCompleted();
        assertThat(postureStatus).isEqualTo(status);

        if (postureStatus == ProgressStatus.Completed)
            assertThat(progress.getTotalProgress()).isEqualTo(1.0);
    }
}
