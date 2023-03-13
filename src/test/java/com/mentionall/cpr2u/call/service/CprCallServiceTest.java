package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.CprCallStatus;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.dto.CprCallNearUserDto;
import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

class CprCallServiceTest {

    private CprCallService cprCallService;
    private CprCallRepository cprCallRepository;
    private UserService userService;
    private UserRepository userRepository;
    private DispatchService dispatchService;
    private DispatchRepository dispatchRepository;

    @Test
    void getNowCallStatusNearUser() {
        //given
        User cprAngelUser = getUser(1);
        User cprAngelUserButNoPatient = getUser(2);
        User yetAngelUser = getUser(3);
        CprCall cprCall1 = new CprCall(cprAngelUser.getAddress(),"fullAddress",LocalDateTime.now(),37.56559872345163, 126.9779734762639);
        CprCall cprCall2 = new CprCall(cprAngelUser.getAddress(),"fullAddress",LocalDateTime.now(),37.56549899694667, 126.97488345790383);
        CprCall cprCall3 = new CprCall(cprAngelUser.getAddress(),"fullAddress",LocalDateTime.now(),37.56520212814079, 126.9771473198163);
        CprCall cprCall4 = new CprCall(yetAngelUser.getAddress(),"fullAddress",LocalDateTime.now(),37.56520212814079, 126.9771473198163);
        CprCall cprCall5 = new CprCall(yetAngelUser.getAddress(),"fullAddress",LocalDateTime.now(),37.56520212814079, 126.9771473198163);

        //when
        CprCallNearUserDto callNearUserDtoForUser1 = cprCallService.getCallNearUser(cprAngelUser.getId());
        CprCallNearUserDto callNearUserDtoForUser2 = cprCallService.getCallNearUser(cprAngelUserButNoPatient.getId());
        CprCallNearUserDto callNearUserDtoForUser3 = cprCallService.getCallNearUser(yetAngelUser.getId());

        //then
        assertThat(callNearUserDtoForUser1.getCprCallDtoList().size()).isEqualTo(3);
        assertThat(callNearUserDtoForUser2.getCprCallDtoList().size()).isEqualTo(0);
        assertThat(callNearUserDtoForUser3.getCprCallDtoList().size()).isEqualTo(0);

    }

    @Test
    void makeCall() {
        //given
        User user = getUser(1);

        //when
        Long callId = cprCallRepository.makeCall(user.getId(), "fullAddress",LocalDateTime.now(),37.56559872345163, 126.9779734762639);

        //then
        CprCall cprCall = cprCallRepository.findById(callId).orElse(null);
        assertThat(cprCall.getStatus()).isEqualTo(CprCallStatus.IN_PROGRESS);

    }

    @Test
    void endCall() {
        //given
        User caller = getUser(1);
        User dispatcher = getUser(2);

        //when
        Long callId = cprCallService.makeCall(caller.getId(), "fullAddress",LocalDateTime.now(),37.56559872345163, 126.9779734762639);
        DispatchResponseDto dispatchInfo = dispatchService.dispatch(dispatcher.getId(), new DispatchRequestDto(callId));
        cprCallService.endCall(callId);

        //then
        CprCall cprCall = cprCallRepository.findById(callId).orElse(null);
        assertThat(cprCall.getStatus()).isEqualTo(CprCallStatus.END_SITUATION);
        Dispatch dispatch = dispatchRepository.findById(dispatchInfo.getDispatchId()).orElse(null);
        assertThat(dispatch.getStatus()).isEqualTo(DispatchStatus.END_SITUATION);

    }


    public User getUser(int number) {
        UserSignUpDto userSignUpDto = new UserSignUpDto("nickname" + number, "phoneNumber" + number, "deviceToken");
        userService.signup(userSignUpDto);
        return  userRepository.findByPhoneNumber("phoneNumber" + number).orElse(null);
    }
}