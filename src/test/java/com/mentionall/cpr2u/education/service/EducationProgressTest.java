package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.dto.ScoreDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.education.repository.FakeEducationProgressRepository;
import com.mentionall.cpr2u.education.repository.FakeLectureRepository;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.FakeUserRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.mentionall.cpr2u.education.domain.ProgressStatus.*;
import static com.mentionall.cpr2u.education.domain.TestStandard.*;
import static com.mentionall.cpr2u.education.domain.TestStandard.totalStep;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EducationProgressTest {
    private EducationProgressService progressService;
    private UserRepository userRepository;
    private LectureRepository lectureRepository;
    private EducationProgressRepository progressRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository = new FakeUserRepository();
        lectureRepository = new FakeLectureRepository();
        progressRepository = new FakeEducationProgressRepository();
        progressService = new EducationProgressService(userRepository, progressRepository, lectureRepository);
    }

    @BeforeEach
    public void insertData() {
        User user = userRepository.save(new User("1L", new UserSignUpDto("현애", "010-9980-6523", "device_token")));
        progressRepository.save(new EducationProgress(user));
        lectureRepository.save(new Lecture(1L, "타이틀", "강의 URL", 1, "설명", new ArrayList<>()));
    }

    @Test
    @DisplayName("강의 이수 완료")
    public void completeLecture() {
        //given
        User user = userRepository.findById("1L").get();

        //when lecture course is not started,
        verifyLectureProgress(user, null, NotCompleted);

        //when lecture course is in progress,
        for (var lecture : lectureRepository.findAll()) {
            progressService.completeLecture(user, lecture.getId());

            int currentStep = progressRepository.findByUser(user).get().getLastLecture().getStep();
            boolean isInProgress = currentStep < finalLectureStep;
            if (isInProgress) verifyLectureProgress(user, lecture, InProgress);
        }

        //when lecture course is completed,
        verifyLectureProgress(user, null, Completed);
    }

    @Test
    @DisplayName("퀴즈 테스트 통과")
    public void completeQuiz() {
        //given
        User user = userRepository.findById("1L").get();
        completeLectureCourse(user);

        //when quiz test is not started,
        verifyQuizProgress(user, NotCompleted);

        //when the user fails the quiz test,
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completeQuiz(user.getId(), new ScoreDto(50)));
        verifyQuizProgress(user, NotCompleted);

        //when the user succeeds the quiz test,
        progressService.completeQuiz(user.getId(), new ScoreDto(100));
        verifyQuizProgress(user, Completed);
    }

    @Test
    @DisplayName("자세실습 테스트 통과")
    public void completePosture() {
        //given
        User user = userRepository.findById("1L").get();
        completeLectureCourse(user);
        progressService.completeQuiz(user.getId(), new ScoreDto(100));

        //when a posture test is not started,
        verifyPostureProgress(user, NotCompleted);

        //when the user fails the posture test,
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user.getId(), new ScoreDto(79)));
        verifyPostureProgress(user, NotCompleted);

        //when the user succeeds the posture test,
        progressService.completePosture(user.getId(), new ScoreDto(81));
        verifyPostureProgress(user, Completed);
    }

    @Test
    @Transactional
    @DisplayName("강의를 듣지 않으면 퀴즈 테스트 불통과")
    public void completeQuizWithoutLecture() {
        //given
        User user = userRepository.findById("1L").get();

        // when the lecture course is not completed,
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completeQuiz(user.getId(), new ScoreDto(100))
        );
    }

    @Test
    @DisplayName("강의/퀴즈를 마무리하지 않으면 자세 실습 불통과")
    public void completePostureWithoutQuizOrLecture() {
        //given
        User user = userRepository.findById("1L").get();

        // when the lecture course is not completed,
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user.getId(), new ScoreDto(100)));

        // when the lecture course is completed, but quiz test is not
        completeLectureCourse(user);
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user.getId(), new ScoreDto(100)));
    }

    private void verifyLectureProgress(User user, Lecture lecture, ProgressStatus status) {
        var progress = progressService.readEducationInfo(user);
        assertThat(progress.getIsLectureCompleted()).isEqualTo(status.ordinal());

        if (status == InProgress) {
            int currentStep = progressRepository.findByUser(user).get().getLastLecture().getStep();
            assertThat(currentStep).isEqualTo(lecture.getStep());
            assertThat(progress.getLastLectureTitle()).isEqualTo(lecture.getTitle());
        }

        double progressPercent =
                (status == Completed) ? ((double) finalLectureStep / (double) totalStep) :
                (status == InProgress) ? (double) lecture.getStep() / (double) totalStep : 0.0;
        assertThat(progress.getProgressPercent()).isEqualTo(progressPercent);
    }

    private void verifyQuizProgress(User user, ProgressStatus status) {
        int quizStatus = progressService.readEducationInfo(user).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(status.ordinal());
    }

    private void verifyPostureProgress(User user, ProgressStatus status) {
        var progress = progressService.readEducationInfo(user);
        int postureStatus = progress.getIsPostureCompleted();
        assertThat(postureStatus).isEqualTo(status.ordinal());

        if (postureStatus == Completed.ordinal())
            assertThat(progress.getProgressPercent()).isEqualTo(1.0);
    }

    private void completeLectureCourse(User user) {
        var lectureList = lectureRepository.findAll().stream().sorted().collect(Collectors.toList());
        lectureList.forEach(lecture ->
                progressService.completeLecture(user, lecture.getId())
        );
    }
}
