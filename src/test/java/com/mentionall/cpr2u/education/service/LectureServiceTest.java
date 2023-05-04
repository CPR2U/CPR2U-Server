package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.education.dto.lecture.LectureRequestDto;
import com.mentionall.cpr2u.education.dto.lecture.LectureResponseDto;
import com.mentionall.cpr2u.education.dto.lecture.PostureLectureResponseDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.dto.UserTokenDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("사용자의 강의 진도 조회")
    public void readLectureProgress() {
        //given
        UserTokenDto tokens = userService.signup(new UserSignUpDto("유저1", "010-1234-1234", "device_token"));
        String userId = jwtTokenProvider.getUserId(tokens.getAccessToken());
        User user = userRepository.findById(userId).get();

        //when
        var progressDto = lectureService.readLectureProgress(user);

        //then
        assertThat(progressDto.getCurrentStep()).isEqualTo(0);
        assertThat(progressDto.getLectureList().size()).isEqualTo(TestStandard.finalLectureStep);
    }
}
