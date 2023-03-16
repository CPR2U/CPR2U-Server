package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CPRCall;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.domain.Report;
import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.dto.ReportRequestDto;
import com.mentionall.cpr2u.call.repository.*;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.FakeUserRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class DispatchServiceTest {

    private DispatchService dispatchService;

    private DispatchRepository dispatchRepository;

    private CprCallRepository callRepository;

    private UserRepository userRepository;

    private ReportRepository reportRepository;

    @BeforeEach
    public void beforeEach() {
        this.dispatchRepository = new FakeDispatchRepository();
        this.callRepository = new FakeCprCallRepository();
        this.userRepository = new FakeUserRepository();
        this.reportRepository = new FakeReportRepository();
        this.dispatchService = new DispatchService(dispatchRepository, callRepository, userRepository, reportRepository);

        // 테스트할 객체 추가
        User user = new User("1L", new UserSignUpDto("현애", "010-9980-6523", "device_token"));
        userRepository.save(user);

        CPRCall cprCall = new CPRCall(1L, "서울시 용산구 청파로 43길 100", 12.44, 36.55);
        callRepository.save(cprCall);
    }

    @Test
    @DisplayName("CPR 출동")
    public void dispatch() {
        //given
        User user = userRepository.findById("1L").orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));
        CPRCall cprCall = callRepository.findById(1L).orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL));

        //when
        DispatchResponseDto response = dispatchService.dispatch(user.getId(), new DispatchRequestDto(cprCall.getId()));

        //then
        assertThat(response.getCalledAt()).isEqualTo(cprCall.getCalledAt());
        assertThat(response.getLatitude()).isEqualTo(12.44);
        assertThat(response.getLongitude()).isEqualTo(36.55);
        assertThat(response.getFullAddress()).isEqualTo("서울시 용산구 청파로 43길 100");

        Dispatch dispatch = dispatchRepository.findById(response.getDispatchId()).get();
        assertThat(dispatch.getStatus()).isEqualTo(DispatchStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("CPR 출동 도착")
    public void arrive() {
        //given
        User user = userRepository.findById("1L").orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));
        CPRCall cprCall = callRepository.findById(1L).orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL));

        //when
        DispatchResponseDto response = dispatchService.dispatch(user.getId(), new DispatchRequestDto(cprCall.getId()));
        dispatchService.arrive(response.getDispatchId());

        //then
        Optional<Dispatch> dispatchArrived = dispatchRepository.findById(response.getDispatchId());
        assertThat(dispatchArrived.get().getStatus()).isEqualTo(DispatchStatus.ARRIVED);
    }

    @Test
    @DisplayName("출동 신고")
    public void report() {
        //given
        User user = userRepository.findById("1L").orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));
        CPRCall cprCall = callRepository.findById(1L).orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL));

        //when
        DispatchResponseDto response = dispatchService.dispatch(user.getId(), new DispatchRequestDto(cprCall.getId()));
        dispatchService.report(new ReportRequestDto(response.getDispatchId(), "신고 내용"));

        //then
        List<Report> reportList = reportRepository.findAllByReporter(user);
        assertThat(reportList.size()).isEqualTo(1);
        assertThat(reportList.get(0).getCprCall().getId()).isEqualTo(1L);
        assertThat(reportList.get(0).getContent()).isEqualTo("신고 내용");
    }
}
