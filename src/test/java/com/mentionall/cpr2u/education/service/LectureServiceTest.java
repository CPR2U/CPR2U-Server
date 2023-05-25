package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.dto.lecture.LectureRequestDto;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.SignUpRequestDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.AddressService;
import com.mentionall.cpr2u.user.service.AuthService;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.mentionall.cpr2u.education.domain.TestStandard.finalLectureStep;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("강의 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LectureServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EducationProgressService progressService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private LectureService lectureService;

    @BeforeEach
    private void beforeEach() {
        addressService.loadAddressList();
    }

    @Test
    @Transactional
    public void 강의를_이수하지_않은_유저가_강의_리스트_조회() {
        //given
        createLectureCourse();

        var address = addressService.readAll().get(0).getGugunList().get(0);
        authService.signup(new SignUpRequestDto("유저1", "010-1234-1234", address.getId(), "device_token"));
        User user = userRepository.findByPhoneNumber("010-1234-1234").get();

        //when
        var lectureInfo = lectureService.readLectureProgressAndList(user);

        //then
        assertThat(lectureInfo.getCurrentStep()).isEqualTo(0);
        assertThat(lectureInfo.getLectureList().size()).isEqualTo(finalLectureStep);
    }

    @Test
    @Transactional
    public void 강의를_이수한_유저가_강의_리스트_조회() {
        //given
        createLectureCourse();

        var address = addressService.readAll().get(0).getGugunList().get(0);
        authService.signup(new SignUpRequestDto("유저1", "010-1234-1234", address.getId(), "device_token"));
        User user = userRepository.findByPhoneNumber("010-1234-1234").get();

        completeFirstLecture(user);

        //when
        var lectureInfo = lectureService.readLectureProgressAndList(user);

        //then
        assertThat(lectureInfo.getCurrentStep()).isEqualTo(1);
        assertThat(lectureInfo.getLectureList().size()).isEqualTo(finalLectureStep);
    }

    private void completeFirstLecture(User user) {
        var lectureInfo = lectureService.readLectureProgressAndList(user);
        var lecture = lectureInfo.getLectureList().get(0);
        progressService.completeLecture(user, lecture.getId());
    }

    private void createLectureCourse() {
        lectureService.createLecture(new LectureRequestDto(1, "일반인 심폐소생술 표준 교육", "2020 Korean Guideline", "https://youtu.be/5DWyihalLMM"));
    }
}
