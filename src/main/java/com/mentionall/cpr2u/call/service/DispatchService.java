package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CPRCall;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.Report;
import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.dto.ReportRequestDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.call.repository.ReportRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mentionall.cpr2u.util.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final DispatchRepository dispatchRepository;
    private final CprCallRepository cprCallRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public DispatchResponseDto dispatch(String userId, DispatchRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
        CPRCall cprCall = cprCallRepository.findById(requestDto.getCprCallId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_CPRCALL)
        );

        Dispatch dispatch = new Dispatch(user, cprCall);
        dispatchRepository.save(dispatch);

        return new DispatchResponseDto(cprCall, dispatch);
    }

    public void arrive(Long dispatchId) {
        Dispatch dispatch = dispatchRepository.findById(dispatchId).orElseThrow(
                () -> new CustomException(NOT_FOUND_DISPATCH)
        );
        dispatch.arrive();
    }

    public void report(ReportRequestDto requestDto) {
        Dispatch dispatch = dispatchRepository.findById(requestDto.getDispatchId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_DISPATCH)
        );

        reportRepository.save(new Report(dispatch.getCprCall(), dispatch.getDispatcher(), requestDto.getContent()));
    }
}
