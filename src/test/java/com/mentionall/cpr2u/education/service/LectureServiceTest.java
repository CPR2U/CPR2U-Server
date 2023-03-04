package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.dto.LectureRequestDto;
import com.mentionall.cpr2u.education.dto.LectureResponseDto;
import com.mentionall.cpr2u.education.dto.LectureProgressDto;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

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
        lectureService.createLecture(new LectureRequestDto(1, "강의1", "1입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(2, "강의2", "2입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(3, "강의3", "3입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(4, "강의4", "4입니다.", "https://naver.com", "THEORY"));
        lectureService.createLecture(new LectureRequestDto(5, "강의5", "자세 강의입니다.", "https://naver.com", "POSTURE"));
    }

    @Test
    @Transactional
    public void readLectureProgress() {
        //given
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        //when
        LectureProgressDto progressDto = lectureService.readLectureProgressList(userId);

        //then
        assertThat(progressDto.getLastStep()).isEqualTo(0);
        assertThat(progressDto.getLectureList().size()).isEqualTo(4);

        int beforeStep = 0;
        for (LectureResponseDto lecture : progressDto.getLectureList()) {
            assertThat(lecture.getStep()).isGreaterThan(beforeStep);
            beforeStep = lecture.getStep();
        }


    }

    @Test
    public void readPostureLecture() {
        //given
        //when
        List<LectureResponseDto> lectureList = lectureService.readPostureLecture();

        //then
        assertThat(lectureList.size()).isEqualTo(1);
        assertThat(lectureList.get(0).getStep()).isEqualTo(5);
    }
}
