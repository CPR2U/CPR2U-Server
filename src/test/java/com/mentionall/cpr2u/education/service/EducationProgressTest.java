package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
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
import java.util.List;

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
    public void beforeEach() {
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
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        //when lecture course is not started,
        List<LectureResponseDto> lectureList = lectureService.readLectureProgressList(userId).getLectureList();

        EducationProgressDto educationProgress = progressService.readEducationInfo(userId);
        assertThat(educationProgress.getIsLectureCompleted()).isEqualTo(ProgressStatus.NotCompleted);
        assertThat(educationProgress.getProgressAll()).isEqualTo(0.0);

        //when lecture course is in progress,
        for (LectureResponseDto lecture : lectureList) {
            progressService.completeLecture(userId, lecture.getId());

            int lastStep = lectureService.readLectureProgressList(userId).getLastStep();
            assertThat(lastStep).isEqualTo(lecture.getStep());

            educationProgress = progressService.readEducationInfo(userId);
            if (lastStep < 4) {
                assertThat(educationProgress.getIsLectureCompleted()).isEqualTo(ProgressStatus.InProgress);
                assertThat(educationProgress.getProgressAll()).isEqualTo((double) lecture.getStep() / 6.0);
                assertThat(educationProgress.getLastLectureTitle()).isEqualTo(lecture.getTitle());
            }
        }

        //when lecture course is completed,
        assertThat(educationProgress.getIsLectureCompleted()).isEqualTo(ProgressStatus.Completed);
        assertThat(educationProgress.getProgressAll()).isEqualTo(4.0 / 6.0);
    }

    @Test
    @Transactional
    public void completeQuiz() {
        //given
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<LectureResponseDto> lectureList = lectureService.readLectureProgressList(userId).getLectureList();
        for (LectureResponseDto lecture : lectureList) {
            progressService.completeLecture(userId, lecture.getId());
        }

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
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<LectureResponseDto> lectureList = lectureService.readLectureProgressList(userId).getLectureList();
        for (LectureResponseDto lecture : lectureList) {
            progressService.completeLecture(userId, lecture.getId());
        }
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
        assertThat(educationProgress.getProgressAll()).isEqualTo(1.0);
        assertThat(educationProgress.getIsPostureCompleted()).isEqualTo(ProgressStatus.Completed);
    }

    public void completeQuizWithoutLecture() {
        // TODO: 강의 수강 여부가 NOT COMPLETED / IN PROGRESS인 경우
    }

    public void completePostureWithoutQuizOrLecture() {
        // TODO: 강의 수강 여부가 NOT COMPLETED / IN PROGRESS / COMPLETED인 경우
    }
}
