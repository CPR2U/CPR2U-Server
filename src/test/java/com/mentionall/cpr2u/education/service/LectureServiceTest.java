package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.TestStandard;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.UserSignUpDto;
import com.mentionall.cpr2u.user.dto.user.UserTokenDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("강의 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    public void 사용자의_강의_리스트를_조회하는_경우() {
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
