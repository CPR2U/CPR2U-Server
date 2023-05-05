package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.*;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("로그인 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String phoneNumber = "010-0000-0000";
    private static final String nickname = "예진";
    private static final String deviceToken = "device-code";

    @Test
    @Transactional
    public void 회원_가입() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);

        //when
        var tokens = userService.signup(userSignUpDto);

        //then
        String userId = jwtTokenProvider.getUserId(tokens.getAccessToken());

        User user = userRepository.findByPhoneNumber(phoneNumber).get();
        assertThat(user.getId()).isEqualTo(userId);
    }

    @Test
    @Transactional
    public void 로그인() {
        //given
        userService.signup(new UserSignUpDto(nickname, phoneNumber, deviceToken));

        //when
        UserTokenDto userTokenDto = userService.login(new UserLoginDto(phoneNumber, deviceToken));

        //then
        String userId = jwtTokenProvider.getUserId(userTokenDto.getAccessToken());
        User findUser = userRepository.findByPhoneNumber(phoneNumber).get();
        assertThat(findUser.getId()).isEqualTo(userId);
    }

    //TODO: 랜덤 코드 생성 테스트 리팩토링(코드 생성 서비스 분리 필요)
    @Test
    @Transactional
    public void 전화번호_인증코드_생성(){
        //given
        var phoneNumberInfo = new UserPhoneNumberDto(phoneNumber);

        for(int i = 0 ; i < 100 ; i ++) {
            //when
            UserCodeDto userCodeDto = userService.getVerificationCode(phoneNumberInfo);

            //then
            assertThat(userCodeDto.getValidationCode().length()).isEqualTo(4);
        }
    }

    @Test
    @Transactional
    public void 자동_로그인() {
        //given
        userService.signup(new UserSignUpDto(nickname, phoneNumber, deviceToken));
        var tokens = userService.login(new UserLoginDto(phoneNumber, deviceToken));

        //when
        var newTokens = userService.reissueToken(new UserTokenReissueDto(tokens.getRefreshToken()));

        //then
        assertThat(jwtTokenProvider.getUserId(tokens.getAccessToken()))
                .isEqualTo(jwtTokenProvider.getUserId(newTokens.getAccessToken()));
    }

    @Test
    @Transactional
    public void 닉네임_중복체크시_중복되는_경우() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);
        userService.signup(userSignUpDto);

        //when
        String newNickname = nickname;

        //then
        Assertions.assertThrows(CustomException.class, () -> {userService.checkNicknameDuplicated(newNickname);});
    }

    @Test
    @Transactional
    public void 닉네임_중복체크시_사용가능한_경우() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto(nickname, phoneNumber, deviceToken);
        userService.signup(userSignUpDto);

        //when
        String newNickname = nickname;

        //then
        Assertions.assertDoesNotThrow(() -> userService.checkNicknameDuplicated(newNickname));
    }
}
