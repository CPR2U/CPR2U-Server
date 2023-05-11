package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.dto.ScoreDto;
import com.mentionall.cpr2u.user.domain.AngelStatus;
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
import static com.mentionall.cpr2u.user.domain.AngelStatus.*;
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
            var educationInfo = progressService.readEducationInfo(user);
            assertThat(educationInfo.getIsLectureCompleted()).isEqualTo(InProgress.ordinal());

            double progressPercent = lecture.getStep() / totalStep;
            assertThat(educationInfo.getProgressPercent()).isEqualTo(progressPercent);
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
        var educationInfo = progressService.readEducationInfo(user);
        assertThat(educationInfo.getIsLectureCompleted()).isEqualTo(Completed.ordinal());
        assertThat(educationInfo.getProgressPercent()).isEqualTo((double)finalLectureStep / (double)totalStep);

        assertThat(educationInfo.getIsQuizCompleted()).isEqualTo(NotCompleted.ordinal());
        assertThat(educationInfo.getIsPostureCompleted()).isEqualTo(NotCompleted.ordinal());
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

    @Test
    @Transactional
    public void 교육_수료_전_수료증_확인() {
        //given
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var educationInfo = progressService.readEducationInfo(user);

        //then
        assertThat(educationInfo.getAngelStatus()).isEqualTo(UNACQUIRED.ordinal());
        assertThat(educationInfo.getDaysLeftUntilExpiration()).isEqualTo(null);
    }

    @Test
    @Transactional
    public void 교육_수료_당일_수료증_확인() {
        //given
        createCertificatedUser(LocalDateTime.now());
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var educationInfo = progressService.readEducationInfo(user);

        //then
        assertThat(educationInfo.getAngelStatus()).isEqualTo(ACQUIRED.ordinal());
        assertThat(educationInfo.getDaysLeftUntilExpiration()).isEqualTo(validTime);
    }

    @Test
    @Transactional
    public void 교육_수료_3일_후_수료증_확인() {
        //given
        int day = 3;
        createCertificatedUser(LocalDate.now().minusDays(day).atStartOfDay());
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var educationInfo = progressService.readEducationInfo(user);

        //then
        assertThat(educationInfo.getAngelStatus()).isEqualTo(ACQUIRED.ordinal());
        assertThat(educationInfo.getDaysLeftUntilExpiration()).isEqualTo(validTime - day);
    }

    @Test
    @Transactional
    public void 교육_수료_90일_후_수료증_확인() {
        //given
        int day = 90;
        createCertificatedUser(LocalDate.now().minusDays(day).atStartOfDay());
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var educationInfo = progressService.readEducationInfo(user);

        //then
        assertThat(educationInfo.getAngelStatus()).isEqualTo(ACQUIRED.ordinal());
        assertThat(educationInfo.getDaysLeftUntilExpiration()).isEqualTo(validTime - day);
    }

    @Test
    @Transactional
    public void 교육_수료_91일_후_수료증_만료() {
        //given
        int day = 91;
        createCertificatedUser(LocalDate.now().minusDays(day).atStartOfDay());
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();

        //when
        var educationInfo = progressService.readEducationInfo(user);

        //then
        // TODO: Scheduler 테스트 코드를 짜거나 또는 예외 상황 코드 추가
        //assertThat(educationInfo.getAngelStatus()).isEqualTo(EXPIRED);
        assertThat(educationInfo.getDaysLeftUntilExpiration()).isEqualTo(null);
    }

    private void completeLectureCourse(User user) {
        var lectureList = lectureService.readLectureProgress(user).getLectureList();
        for (var lecture : lectureList) {
            progressService.completeLecture(user, lecture.getId());
        }
    }

    private void createCertificatedUser(LocalDateTime time) {
        userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device_token"));
        User user = userRepository.findByPhoneNumber("010-0000-0000").get();
        userService.certificate(user, time);
    }
}
