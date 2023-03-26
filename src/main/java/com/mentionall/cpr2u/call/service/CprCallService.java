package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.dto.CprCallResponseDto;
import com.mentionall.cpr2u.call.dto.CprCallIdResponseDto;
import com.mentionall.cpr2u.call.dto.CprCallNearUserResponseDto;
import com.mentionall.cpr2u.call.dto.CprCallRequestDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.AddressRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CprCallService {
    private final CprCallRepository cprCallRepository;
    private final DispatchRepository dispatchRepository;
    private final AddressRepository addressRepository;

    public CprCallNearUserResponseDto getCallNearUser(User user) {
        AngelStatusEnum userAngelStatus = user.getStatus();
        if(userAngelStatus != AngelStatusEnum.ACQUIRED){
            return new CprCallNearUserResponseDto(
                    userAngelStatus,
                    false,
                    new ArrayList<>()
            );
        }
        if(user.getAddress() == null){
            throw new CustomException(ResponseCode.BAD_REQUEST_ADDRESS_NOT_SET);
        }
        List<CprCallResponseDto> cprCallResponseDtoList = cprCallRepository.findAllCallInProcessByAddress(user.getAddress().getId());
        return new CprCallNearUserResponseDto(
                userAngelStatus,
                cprCallResponseDtoList.size() > 0,
                cprCallResponseDtoList
        );
    }

    public CprCallIdResponseDto makeCall(CprCallRequestDto cprCallRequestDto, User user) {
        Address callAddress = addressRepository.findByFullAddress(cprCallRequestDto.getFullAddress().split(" "))
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_FAILED_TO_FIND_ADDRESS));
        CprCall cprCall = new CprCall(user, callAddress, LocalDateTime.now(), cprCallRequestDto);
        cprCallRepository.save(cprCall);
        return new CprCallIdResponseDto(cprCall.getId());
    }

    public void endCall(Long callId) {
        CprCall cprCall = cprCallRepository.findById(callId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL)
        );
        //TODO FCM 붙이기
        cprCall.endSituationCprCall();
        cprCallRepository.save(cprCall);
        List<Dispatch> dispatchList = dispatchRepository.findAllByCprCallId(cprCall.getId());
        for(Dispatch dispatch : dispatchList){
            dispatch.setStatus(DispatchStatus.END_SITUATION);
            dispatchRepository.save(dispatch);
        }

    }
}
