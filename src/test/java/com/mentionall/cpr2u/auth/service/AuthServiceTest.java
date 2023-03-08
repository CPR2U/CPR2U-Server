package com.mentionall.cpr2u.auth.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.*;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String phoneNumber = "010-0000-0000";
    private String nickname = "예진";
    private String deviceToken = "device-code";

    @Test
    @Transactional
    public void signup() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);

        //when
        UserTokenDto userTokenDto = userService.signup(userSignUpDto);

        //then
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
        String userId1 = user.getId();
        String userId2 = jwtTokenProvider.getUserId(userTokenDto.getAccessToken());
        assertThat(userId1).isEqualTo(userId2);
    }

    @Test
    @Transactional
    public void login() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);
        userService.signup(userSignUpDto);
        UserLoginDto userLoginDto = new UserLoginDto(phoneNumber, deviceToken);

        //when
        UserTokenDto userTokenDto = userService.login(userLoginDto);

        //then
        User user = userRepository.findByPhoneNumber(phoneNumber).orElse(null);
        String userId1 = user.getId();
        String userId2 = jwtTokenProvider.getUserId(userTokenDto.getAccessToken());
        assertThat(userId1).isEqualTo(userId2);
    }

    @Test
    @Transactional
    public void verification(){
        //given
        for(int i = 0 ; i < 100 ; i ++) {
            //when
            UserCodeDto userCodeDto = userService.getVerificationCode();

            //then
            assertThat(userCodeDto.getValidationCode().length()).isEqualTo(4);
        }
    }

    @Test
    @Transactional
    public void autoLogin() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);
        userService.signup(userSignUpDto);
        UserLoginDto userLoginDto = new UserLoginDto(phoneNumber, deviceToken);
        UserTokenDto userTokenDto = userService.login(userLoginDto);
        String userId = jwtTokenProvider.getUserId(userTokenDto.getAccessToken());
        UserTokenReissueDto userTokenReissueDto = new UserTokenReissueDto(userTokenDto.getRefreshToken());

        //when
        UserTokenDto newUserTokenDto = userService.reissueToken(userTokenReissueDto);

        //then
        assertThat(userId).isEqualTo(jwtTokenProvider.getUserId(newUserTokenDto.getAccessToken()));
    }
}
