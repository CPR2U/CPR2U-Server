package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.CprCallStatus;
import com.mentionall.cpr2u.call.dto.CprCallDto;
import com.mentionall.cpr2u.call.dto.CprCallIdDto;
import com.mentionall.cpr2u.call.dto.CprCallNearUserDto;
import com.mentionall.cpr2u.call.dto.CprCallOccurDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final CprCallRepository cprCallRepository;

    public CprCallNearUserDto getCallNearUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        AngelStatusEnum userAngelStatus = user.getStatus();
        if(userAngelStatus != AngelStatusEnum.ACQUIRED){
            return new CprCallNearUserDto(
                    userAngelStatus,
                    false,
                    new ArrayList<>()
            );
        }
        if(user.getAddress() == null){
            throw new CustomException(ResponseCode.BAD_REQUEST_ADDRESS_NOT_SET);
        }
        List<CprCallDto> cprCallDtoList = cprCallRepository.findAllByStatusAndAddress(CprCallStatus.IN_PROGRESS, user.getAddress().getId());
        return new CprCallNearUserDto(
                userAngelStatus,
                cprCallDtoList.size() > 0 ? true : false,
                cprCallDtoList
        );
    }

    public CprCallIdDto makeCall(CprCallOccurDto cprCallOccurDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER)
        );
        Address callAddress = null;
        //TODO Address 찾아오기
        CprCall cprCall = new CprCall(user, callAddress, LocalDateTime.now(), cprCallOccurDto);
        return new CprCallIdDto(cprCall.getId());
    }
}
