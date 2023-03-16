package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.CprCallStatus;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.dto.CprCallNearUserDto;
import com.mentionall.cpr2u.call.dto.CprCallOccurDto;
import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.AddressRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CprCallServiceTest {

    @Autowired
    private CprCallService cprCallService;
    @Autowired
    private CprCallRepository cprCallRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DispatchService dispatchService;
    @Autowired
    private DispatchRepository dispatchRepository;
    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    public void beforeEach(){
        User user1 = registerUser(1);
        User user2 = registerUser(2);
        User user3 = registerUser(3);
        User user4 = registerUser(4);
    }

    @Test
    @Transactional
    @DisplayName("엔젤 유저들의 근처 호출 조회")
    void getNowCallStatusNearUser() {
        //given
        User cprAngelUser = userRepository.findByPhoneNumber("phoneNumber" + 1).get();
        cprAngelUser.setAddress(addressRepository.findById(1L).get());
        cprAngelUser.setUserAngelStatus(AngelStatusEnum.ACQUIRED);
        userRepository.save(cprAngelUser);

        User cprAngelUserButNoPatient = userRepository.findByPhoneNumber("phoneNumber" + 2).get();
        cprAngelUserButNoPatient.setAddress(addressRepository.findById(2L).get());
        cprAngelUserButNoPatient.setUserAngelStatus(AngelStatusEnum.ACQUIRED);
        userRepository.save(cprAngelUserButNoPatient);

        User yetAngelUser = userRepository.findByPhoneNumber("phoneNumber" + 3).get();
        yetAngelUser.setAddress(addressRepository.findById(1L).get());
        userRepository.save(yetAngelUser);

        User caller = userRepository.findByPhoneNumber("phoneNumber" + 4).get();

        CprCall cprCall1 = new CprCall(caller, cprAngelUser.getAddress(), LocalDateTime.now(), new CprCallOccurDto("fullAddress", 37.56559872345163, 126.9779734762639));
        CprCall cprCall2 = new CprCall(caller, cprAngelUser.getAddress(), LocalDateTime.now(), new CprCallOccurDto("fullAddress", 37.56520212814079, 126.9771473198163));
        CprCall cprCall3 = new CprCall(caller, cprAngelUser.getAddress(), LocalDateTime.now(), new CprCallOccurDto("fullAddress", 37.56549899694667, 126.97488345790383));
        CprCall cprCall4 = new CprCall(caller, cprAngelUser.getAddress(), LocalDateTime.now(), new CprCallOccurDto("fullAddress", 37.56520212814079, 126.9771473198163));

        cprCallRepository.save(cprCall1);
        cprCallRepository.save(cprCall2);
        cprCallRepository.save(cprCall3);
        cprCallRepository.save(cprCall4);

        cprCallService.endCall(cprCall3.getId());

        //when
        CprCallNearUserDto callNearUserDtoForUser1 = cprCallService.getCallNearUser(cprAngelUser);
        CprCallNearUserDto callNearUserDtoForUser2 = cprCallService.getCallNearUser(cprAngelUserButNoPatient);
        CprCallNearUserDto callNearUserDtoForUser3 = cprCallService.getCallNearUser(yetAngelUser);

        //then
        assertThat(callNearUserDtoForUser1.getCprCallDtoList().size()).isEqualTo(3);
        assertThat(callNearUserDtoForUser2.getCprCallDtoList().size()).isEqualTo(0);
        assertThat(callNearUserDtoForUser3.getCprCallDtoList().size()).isEqualTo(0);

    }

    @Test
    @Transactional
    @DisplayName("호출 생성")
    void makeCall() {
        //given
        User user = registerUser(1);

        //when
        Long callId1 = cprCallService.makeCall(new CprCallOccurDto("서울 종로구 종로 104", 37.56559872345163, 126.9779734762639), user).getCallId();
        Long callId2 = cprCallService.makeCall(new CprCallOccurDto("서울 중구 세종대로 지하 2", 37.56559872345163, 126.9779734762639), user).getCallId();
        Long callId3 = cprCallService.makeCall(new CprCallOccurDto("세종특별자치시 한누리대로 2130 (우)30151", 37.56559872345163, 126.9779734762639), user).getCallId();
        Long callId4 = cprCallService.makeCall(new CprCallOccurDto("경남 창원시 진해구 평안동 10", 37.56559872345163, 126.9779734762639), user).getCallId();

        //then
        CprCall cprCall1 = cprCallRepository.findById(callId1).get();
        CprCall cprCall2 = cprCallRepository.findById(callId2).get();
        CprCall cprCall3 = cprCallRepository.findById(callId3).get();
        CprCall cprCall4 = cprCallRepository.findById(callId4).get();

        assertThat(cprCall1.getAddress().getId()).isEqualTo(1L);
        assertThat(cprCall2.getAddress().getId()).isEqualTo(2L);
        assertThat(cprCall3.getAddress().getId()).isEqualTo(75L);
        assertThat(cprCall4.getAddress().getId()).isEqualTo(231L);
        assertThat(cprCall1.getStatus()).isEqualTo(CprCallStatus.IN_PROGRESS);

    }

    @Test
    @Transactional
    @DisplayName("호출 종료")
    void endCall() {
        //given
        User caller = userRepository.findByPhoneNumber("phoneNumber" + 1).get();
        User dispatcher = userRepository.findByPhoneNumber("phoneNumber" + 2).get();

        //when
        Long callId = cprCallService.makeCall(new CprCallOccurDto("fullAddress", 37.56559872345163, 126.9779734762639), caller).getCallId();
        DispatchResponseDto dispatchInfo = dispatchService.dispatch(dispatcher, new DispatchRequestDto(callId));
        cprCallService.endCall(callId);

        //then
        CprCall cprCall = cprCallRepository.findById(callId).get();
        assertThat(cprCall.getStatus()).isEqualTo(CprCallStatus.END_SITUATION);
        Dispatch dispatch = dispatchRepository.findById(dispatchInfo.getDispatchId()).get();
        assertThat(dispatch.getStatus()).isEqualTo(DispatchStatus.END_SITUATION);

    }

    public User registerUser(int number) {
        UserSignUpDto userSignUpDto = new UserSignUpDto("nickname" + number, "phoneNumber" + number, "deviceToken");
        userService.signup(userSignUpDto);
        return userRepository.findByPhoneNumber("phoneNumber" + number).get();
    }
}