package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.*;
import com.mentionall.cpr2u.call.dto.CprCallOccurDto;
import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.ReportRequestDto;
import com.mentionall.cpr2u.call.repository.*;
import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.address.AddressRepository;
import com.mentionall.cpr2u.user.repository.FakeAddressRepository;
import com.mentionall.cpr2u.user.repository.FakeUserRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DispatchServiceTest {

    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private UserService userService;

    @Autowired
    private DispatchRepository dispatchRepository;

    @Autowired
    private CprCallService callService;

    @Autowired
    private CprCallRepository callRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    private static final String fullAddress = "서울특별시 용산구 청파로47길 100";
    private static final double latitude = 37.545183430559604;
    private static final double longitude = 126.9648022541866;

    @Test
    @DisplayName("CPR 출동")
    @Transactional
    public void dispatch() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber(("010-0000-0000")).get();
        User dispatcher = userRepository.findByPhoneNumber(("010-0000-0001")).get();

        var callIdDto = callService.makeCall(new CprCallOccurDto(fullAddress, latitude, longitude), caller);
        CprCall cprCall = callRepository.findById(callIdDto.getCallId()).get();

        //when
        var cprCallInfo = dispatchService.dispatch(dispatcher, new DispatchRequestDto(cprCall.getId()));

        //then
        assertThat(cprCallInfo.getCalledAt()).isEqualTo(cprCall.getCalledAt());
        assertThat(cprCallInfo.getLatitude()).isEqualTo(latitude);
        assertThat(cprCallInfo.getLongitude()).isEqualTo(longitude);
        assertThat(cprCallInfo.getFullAddress()).isEqualTo(fullAddress);

        Dispatch dispatch = dispatchRepository.findById(cprCallInfo.getDispatchId()).get();
        assertThat(dispatch.getStatus()).isEqualTo(DispatchStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("CPR 출동 도착")
    @Transactional
    public void arrive() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber(("010-0000-0000")).get();
        User dispatcher = userRepository.findByPhoneNumber(("010-0000-0001")).get();

        var callIdDto = callService.makeCall(new CprCallOccurDto(fullAddress, latitude, longitude), caller);
        CprCall cprCall = callRepository.findById(callIdDto.getCallId()).get();

        var response = dispatchService.dispatch(dispatcher, new DispatchRequestDto(cprCall.getId()));

        //when
        dispatchService.arrive(response.getDispatchId());

        //then
        var dispatchArrived = dispatchRepository.findById(response.getDispatchId()).get();
        assertThat(dispatchArrived.getStatus()).isEqualTo(DispatchStatus.ARRIVED);
    }

    @Test
    @DisplayName("출동 신고")
    @Transactional
    public void report() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber(("010-0000-0000")).get();
        User dispatcher = userRepository.findByPhoneNumber(("010-0000-0001")).get();

        var callIdDto = callService.makeCall(new CprCallOccurDto(fullAddress, latitude, longitude), caller);
        CprCall cprCall = callRepository.findById(callIdDto.getCallId()).get();

        var response = dispatchService.dispatch(dispatcher, new DispatchRequestDto(cprCall.getId()));

        //when
        dispatchService.report(new ReportRequestDto(response.getDispatchId(), "신고 내용"));

        //then
        List<Report> reportList = reportRepository.findAllByCprCall(cprCall);
        assertThat(reportList.size()).isEqualTo(1);
        assertThat(reportList.get(0).getReporter().getId()).isEqualTo(dispatcher.getId());
        assertThat(reportList.get(0).getContent()).isEqualTo("신고 내용");
    }

    private void createUsers() {
        var tokens = userService.signup(new UserSignUpDto("호출자", "010-0000-0000", "device_token"));
        var tokens2 = userService.signup(new UserSignUpDto("출동자", "010-0000-0001", "device_token"));
    }
}
