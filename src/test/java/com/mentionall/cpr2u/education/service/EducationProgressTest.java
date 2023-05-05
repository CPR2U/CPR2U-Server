package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.dto.ScoreDto;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import com.mentionall.cpr2u.util.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mentionall.cpr2u.education.domain.ProgressStatus.*;
import static com.mentionall.cpr2u.education.domain.TestStandard.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("유저 교육 진도 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EducationProgressTest {
    @Autowired
    private EducationProgressService progressService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureService lectureService;

    @Test
    @Transactional
    public void 강의_수강중인_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var lectureList = lectureService.readLectureProgress(user).getLectureList();
        for (var lecture : lectureList) {
            progressService.completeLecture(user, lecture.getId());
            if (lecture.getStep() == finalLectureStep) break;

            // then
            var progress = progressService.readEducationInfo(user);
            assertThat(progress.getIsLectureCompleted()).isEqualTo(InProgress.ordinal());

            double progressPercent = lecture.getStep() / totalStep;
            assertThat(progress.getProgressPercent()).isEqualTo(progressPercent);
        }
    }

    @Test
    @Transactional
    public void 강의_수강완료한_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        completeLectureCourse(user);

        //then
        var progress = progressService.readEducationInfo(user);
        assertThat(progress.getIsLectureCompleted()).isEqualTo(Completed.ordinal());
        assertThat(progress.getProgressPercent()).isEqualTo((double)finalLectureStep / (double)totalStep);

        assertThat(progress.getIsQuizCompleted()).isEqualTo(NotCompleted.ordinal());
        assertThat(progress.getIsPostureCompleted()).isEqualTo(NotCompleted.ordinal());
    }

    @Test
    @Transactional
    public void 퀴즈_100점을_넘은_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();
        completeLectureCourse(user);

        //when
        progressService.completeQuiz(user, new ScoreDto(100));

        //then
        var quizStatus = progressService.readEducationInfo(user).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(Completed.ordinal());

        var postureStatus = progressService.readEducationInfo(user).getIsPostureCompleted();
        assertThat(postureStatus).isEqualTo(NotCompleted.ordinal());
    }

    @Test
    @Transactional
    public void 퀴즈_100점을_넘지_않은_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();
        completeLectureCourse(user);

        //when
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completeQuiz(user, new ScoreDto(99)));

        //then
        var quizStatus = progressService.readEducationInfo(user).getIsQuizCompleted();
        assertThat(quizStatus).isEqualTo(NotCompleted.ordinal());
    }

    @Test
    @Transactional
    public void 퀴즈_강의를_마무리하지_않고_테스트한_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when, then
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completeQuiz(user, new ScoreDto(100))
        );
    }

    @Test
    @Transactional
    public void 자세실습_80점을_넘은_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        completeLectureCourse(user);
        progressService.completeQuiz(user, new ScoreDto(100));

        //when
        progressService.completePosture(user, new ScoreDto(81));

        //then
        int postureStatus =  progressService.readEducationInfo(user).getIsPostureCompleted();
        assertThat(postureStatus).isEqualTo(Completed.ordinal());

        double progressPercent = progressService.readEducationInfo(user).getProgressPercent();
        assertThat(progressPercent).isEqualTo(1.0);
    }

    @Test
    @Transactional
    public void 자세실습_80점을_넘지않은_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();
        completeLectureCourse(user);
        progressService.completeQuiz(user, new ScoreDto(100));

        //when
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user, new ScoreDto(79)));

        //then
        int postureStatus =  progressService.readEducationInfo(user).getIsPostureCompleted();
        assertThat(postureStatus).isEqualTo(NotCompleted.ordinal());
    }

    @Test
    @Transactional
    public void 자세실습_강의를_마무리하지_않고_테스트한_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when, then
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user, new ScoreDto(100)));
    }

    @Test
    @Transactional
    public void 자세실습_퀴즈를_마무리하지_않고_테스트한_경우() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when, then
        completeLectureCourse(user);
        Assertions.assertThrows(CustomException.class,
                () -> progressService.completePosture(user, new ScoreDto(100)));
    }

    // TODO: refreshtoken 생성 중에 에러 뜸
    //@Test
    @DisplayName("엔젤 유효기간 D-DAY 값 확인")
    @Transactional
    public void checkAngelStatusDDay() {
        //given
        create4CertificatedUsers();

        User todayUser = userRepository.findByPhoneNumber("010-0000-0000").get();
        User after3DaysUser = userRepository.findByPhoneNumber("010-1111-0000").get();
        User after90DaysUser = userRepository.findByPhoneNumber("010-2222-0000").get();
        User after91DaysUser = userRepository.findByPhoneNumber("010-3333-0000").get();

        //when
        var todayUserInfo = progressService.readEducationInfo(todayUser);
        var after3DayUserInfo = progressService.readEducationInfo(after3DaysUser);
        var after90DaysUserInfo = progressService.readEducationInfo(after90DaysUser);
        var after91DaysUserInfo = progressService.readEducationInfo(after91DaysUser);

        //then
        assertThat(todayUserInfo.getDaysLeftUntilExpiration()).isEqualTo(90);
        assertThat(after3DayUserInfo.getDaysLeftUntilExpiration()).isEqualTo(87);
        assertThat(after90DaysUserInfo.getDaysLeftUntilExpiration()).isEqualTo(0);
        assertThat(after91DaysUserInfo.getDaysLeftUntilExpiration()).isEqualTo(null);
    }

    private void completeLectureCourse(User user) {
        var lectureList = lectureService.readLectureProgress(user).getLectureList();
        for (var lecture : lectureList) {
            progressService.completeLecture(user, lecture.getId());
        }
    }

    private void create4CertificatedUsers() {
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user0 = userRepository.findByPhoneNumber("010-0000-0000").get();
        user0.acquireCertification(LocalDateTime.now());

        userService.signup(new UserSignUpDto("예진", "010-1111-0000", "device_token"));
        User user1 = userRepository.findByPhoneNumber("010-1111-0000").get();
        user1.acquireCertification(LocalDate.now().minusDays(3).atStartOfDay());

        userService.signup(new UserSignUpDto("정현", "010-2222-0000", "device_token"));
        User user2 = userRepository.findByPhoneNumber("010-2222-0000").get();
        user2.acquireCertification(LocalDate.now().minusDays(90).atStartOfDay());

        userService.signup(new UserSignUpDto("채영", "010-3333-0000", "device_token"));
        User user3 = userRepository.findByPhoneNumber("010-3333-0000").get();
        user3.acquireCertification(LocalDate.now().minusDays(91).atStartOfDay());
    }
}
