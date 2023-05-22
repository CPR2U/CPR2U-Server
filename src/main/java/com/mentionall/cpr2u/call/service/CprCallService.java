package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.dto.cpr_call.*;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.AngelStatus;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.address.AddressRepository;
import com.mentionall.cpr2u.user.repository.device_token.DeviceTokenRepository;
import com.mentionall.cpr2u.util.MessageEnum;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import com.mentionall.cpr2u.util.fcm.FcmPushDataType;
import com.mentionall.cpr2u.util.fcm.FcmPushType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CprCallService {
    private final CprCallRepository cprCallRepository;
    private final DispatchRepository dispatchRepository;
    private final AddressRepository addressRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public CprCallNearUserResponseDto getCallNearUser(User user) {
        
        AngelStatus userAngelStatus = user.getAngelStatus();
        if (userAngelStatus != AngelStatus.ACQUIRED) {
            return new CprCallNearUserResponseDto(
                    userAngelStatus,
                    false,
                    new ArrayList<>()
            );
        }

        List<CprCallResponseDto> cprCallResponseDtoList = cprCallRepository.findAllCallInProcessByAddress(user.getAddress().getId());
        return new CprCallNearUserResponseDto(
                userAngelStatus,
                cprCallResponseDtoList.size() > 0,
                cprCallResponseDtoList
        );
    }

    public CprCallIdResponseDto makeCall(CprCallRequestDto requestDto, User user) {
        Address callAddress = addressRepository.findByFullAddress(requestDto.getFullAddress().split(" "))
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_FAILED_TO_MATCH_ADDRESS));

        CprCall cprCall = new CprCall(user, callAddress, LocalDateTime.now(), requestDto);
        cprCallRepository.save(cprCall);

        List<DeviceToken> deviceTokenToSendPushList = deviceTokenRepository.findAllDeviceTokenByUserAddress(cprCall.getAddress().getId(), user.getId());
        for(DeviceToken deviceToken : deviceTokenToSendPushList){
            try {
                firebaseCloudMessageService.sendMessageTo(deviceToken.getToken(),
                        MessageEnum.CPR_CALL_TITLE.getMessage(),
                        cprCall.getFullAddress(),
                        new LinkedHashMap<String,String>(){{
                            put("type", String.valueOf(FcmPushType.CPR_CALL.ordinal()));
                            put("call", String.valueOf(cprCall.getId()));
                        }}
                );
            } catch (IOException e) {
                throw new CustomException(ResponseCode.SERVER_ERROR_FAILED_TO_SEND_FCM);
            }

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    cprCall.endSituationCprCall();
                    cprCallRepository.save(cprCall);
                }
            };

            timer.schedule(task, 1000 * 60 * 10);
        }
        return new CprCallIdResponseDto(cprCall.getId());
    }
  
    public void endCall(Long callId) {
        CprCall cprCall = cprCallRepository.findById(callId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL)
        );
        cprCall.endSituationCprCall();
        cprCallRepository.save(cprCall);
        List<Dispatch> dispatchList = dispatchRepository.findAllByCprCallId(cprCall.getId());
        for (Dispatch dispatch : dispatchList) {
            dispatch.setStatus(DispatchStatus.END_SITUATION);
            dispatchRepository.save(dispatch);
        }

    }

    public CprCallGuideResponseDto getNumberOfAngelsDispatched(Long callId) {
        cprCallRepository.findById(callId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_CPRCALL)
        );

        List<Dispatch> dispatchList = dispatchRepository.findAllByCprCallId(callId);
        return new CprCallGuideResponseDto(dispatchList.size());
    }

    public CprCallIdResponseDto makeCall(CprCallRequestDto cprCallRequestDto, User user) {
        Address callAddress = addressRepository.findByFullAddress(cprCallRequestDto.getFullAddress().split(" "))
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_FAILED_TO_MATCH_ADDRESS));

        CprCall cprCall = new CprCall(user, callAddress, LocalDateTime.now(), cprCallRequestDto);
        cprCallRepository.save(cprCall);

        sendFcmPushToAddress(cprCall, user.getId());

        endCprCallAfterMinutes(cprCall, 10);

        return new CprCallIdResponseDto(cprCall.getId());
    }

    private void sendFcmPushToAddress(CprCall cprCall, String userId) {

        int offset = 0;
        int maxSize = 500;
        Pageable pageable;

        LinkedHashMap<String, String> dataToSend = new LinkedHashMap<>() {{
            put(FcmPushDataType.TYPE.getType(), String.valueOf(FcmPushType.CPR_CALL.ordinal()));
            put(FcmPushDataType.CPR_CALL_ID.getType(), String.valueOf(cprCall.getId()));
        }};

        List<String> deviceTokenToSendPushList;
        do {
            pageable = PageRequest.of(offset, maxSize);
            deviceTokenToSendPushList = deviceTokenRepository.findAllDeviceTokenByUserAddress(cprCall.getAddress().getId(), userId, pageable);
            firebaseCloudMessageService.sendFcmMessage(
                    deviceTokenToSendPushList,
                    MessageEnum.CPR_CALL_TITLE.getMessage(),
                    cprCall.getFullAddress(),
                    dataToSend
            );
            offset += maxSize;
        } while (deviceTokenToSendPushList.size() >= maxSize);

    }

    private void endCprCallAfterMinutes(CprCall cprCall, Integer minutes) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            cprCall.endSituationCprCall();
            cprCallRepository.save(cprCall);
        };

        executor.schedule(task, minutes, TimeUnit.MINUTES);
        executor.shutdown();
    }

}
