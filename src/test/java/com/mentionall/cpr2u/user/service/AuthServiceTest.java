package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.*;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    @Autowired
    private AddressService addressService;

    private static final String phoneNumber = "010-0000-0000";
    private static final String nickname = "예진";
    private static final Long addressId = 1L;
    private static final String deviceToken = "device-code";

    @BeforeEach
    private void beforeEach() {
        addressService.loadAddressList();
    }

    @Test
    @Transactional
    public void 회원가입() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken);

        //when
        userService.signup(signUpRequestDto).getAccessToken();

        //then
        User user = userRepository.findByPhoneNumber(phoneNumber).get();

        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getDeviceToken().getToken()).isEqualTo(deviceToken);
        assertThat(user.getEducationProgress()).isNotNull();
        assertThat(user.getAddress().getId()).isEqualTo(addressId);
    }

    @Test
    @Transactional
    public void 전화번호_중복_회원_회원가입() {

        //given
        Long afAddressId = 2L;
        String afNickname = "현애";
        SignUpRequestDto bfSignUpRequestDto = new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken);
        SignUpRequestDto afSignUpRequestDto = new SignUpRequestDto(afNickname, phoneNumber, afAddressId, deviceToken);

        //when
        userService.signup(bfSignUpRequestDto).getAccessToken();
        User bfUser = userRepository.findByPhoneNumber(phoneNumber).get();
        bfUser.getEducationProgress().getQuizProgress().updateScore(50);
        userService.signup(afSignUpRequestDto).getAccessToken();

        //then
        User afUser = userRepository.findByPhoneNumber(phoneNumber).get();
        assertThat(afUser.getEducationProgress().getQuizProgress().getScore()).isEqualTo(0);
        assertThat(afUser.getNickname()).isEqualTo(afNickname);
        assertThat(afUser.getEducationProgress()).isNotNull();
        assertThat(afUser.getAddress().getId()).isEqualTo(afAddressId);
    }

    @Test
    @Transactional
    public void 로그인() {
        //given
        userService.signup(new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken));

        //when
        var accessToken = userService.login(new LoginRequestDto(phoneNumber, deviceToken)).getAccessToken();

        //then
        User findUser = userRepository.findByPhoneNumber(phoneNumber).get();
        assertThat(findUser.getId()).isEqualTo(jwtTokenProvider.getUserId(accessToken));
        assertThat(findUser.getDeviceToken().getToken()).isEqualTo(deviceToken);
    }

    //TODO: 랜덤 코드 생성 테스트 리팩토링(코드 생성 서비스 분리 필요)
    //TODO: Twillo Fake 객체로 테스트
    //@Test
    @Transactional
    public void 전화번호_인증코드_생성(){
        //given
        var phoneNumberInfo = new PhoneNumberRequestDto(phoneNumber);

        for(int i = 0 ; i < 100 ; i ++) {
            //when
            CodeResponseDto codeResponseDto = userService.getVerificationCode(phoneNumberInfo);

            //then
            assertThat(codeResponseDto.getValidationCode().length()).isEqualTo(4);
        }
    }

    @Test
    @Transactional
    public void 자동_로그인() {
        //given
        userService.signup(new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken));
        var tokens = userService.login(new LoginRequestDto(phoneNumber, deviceToken));

        //when
        var newTokens = userService.reissueToken(new TokenReissueRequestDto(tokens.getRefreshToken()));

        //then
        assertThat(jwtTokenProvider.getUserId(tokens.getAccessToken()))
                .isEqualTo(jwtTokenProvider.getUserId(newTokens.getAccessToken()));
    }

    @Test
    @Transactional
    public void 닉네임_중복체크시_중복되는_경우() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(nickname, phoneNumber, 1L, deviceToken);
        userService.signup(signUpRequestDto);

        //when
        String newNickname = nickname;

        //then
        Assertions.assertThrows(CustomException.class, () -> {userService.checkNicknameDuplicated(newNickname);});
    }

    @Test
    @Transactional
    public void 닉네임_중복체크시_사용가능한_경우() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken);
        userService.signup(signUpRequestDto);

        //when
        String newNickname = "new" + nickname;

        //then
        assertDoesNotThrow(() -> userService.checkNicknameDuplicated(newNickname));
    }

    @Test
    @Transactional
    public void 로그아웃() {
        //given
        userService.signup(new SignUpRequestDto(nickname, phoneNumber, addressId, deviceToken));
        User user = userRepository.findByPhoneNumber(phoneNumber).get();

        //when
        userService.logout(user);

        //then
        assertThat(user.getRefreshToken().getToken()).isEqualTo("expired");
    }
}
