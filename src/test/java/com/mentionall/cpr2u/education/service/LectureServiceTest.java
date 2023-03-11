package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.education.dto.lecture.LectureRequestDto;
import com.mentionall.cpr2u.education.dto.lecture.LectureResponseDto;
import com.mentionall.cpr2u.education.dto.LectureProgressDto;
import com.mentionall.cpr2u.education.dto.lecture.PostureLectureResponseDto;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class LectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void beforeEach() {
        lectureRepository.deleteAll();
        lectureService.createLecture(new LectureRequestDto(1, "강의1", "1입니다.", "https://naver.com"));
    }

    @Test
    @Transactional
    @DisplayName("사용자의 강의 진도 조회")
    public void readLectureProgress() {
        //given
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000", "device-token");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        //when
        LectureProgressDto progressDto = lectureService.readLectureProgress(userId);

        //then
        assertThat(progressDto.getCurrentStep()).isEqualTo(0);
        assertThat(progressDto.getLectureList().size()).isEqualTo(TestStandard.finalLectureStep);

        int beforeStep = 0;
        for (LectureResponseDto lecture : progressDto.getLectureList()) {
            assertThat(lecture.getStep()).isGreaterThan(beforeStep);
            beforeStep = lecture.getStep();
        }
    }

    @Test
    @DisplayName("자세실습 강의 조회")
    public void readPostureLecture() {
        //given & when
        PostureLectureResponseDto postureLecture = lectureService.readPostureLecture();

        //then
        assertThat(postureLecture.getVideoUrl()).isEqualTo("https://www.naver.com");
    }
}
