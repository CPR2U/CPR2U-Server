package com.mentionall.cpr2u.manager;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.util.fcm.FcmPushType;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.service.FirebaseCloudMessageService;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;

import static com.mentionall.cpr2u.education.domain.TestStandard.validTime;
import static com.mentionall.cpr2u.user.domain.AngelStatus.ACQUIRED;
import static com.mentionall.cpr2u.util.MessageEnum.ANGEL_EXPIRED_BODY;
import static com.mentionall.cpr2u.util.MessageEnum.ANGEL_EXPIRED_TITLE;
import static com.mentionall.cpr2u.util.exception.ResponseCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final CprCallRepository cprCallRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Scheduled(cron = "1 0 0 * * *")
    public void updateAngelStatus() {
        List<User> angelList = userRepository.findAllByCertificateStatus(ACQUIRED);

        for (User angel : angelList) {
            LocalDate issuedAt = angel.getCertificate().getDateOfIssue().toLocalDate();
            long currentTime = ChronoUnit.DAYS.between(issuedAt, LocalDate.now());

            if (currentTime > validTime) {
                angel.expireCertificate();
                userRepository.save(angel);

                sendExpiredFcm(angel);
            }
        }
    }

    @Scheduled(cron = "1 0 0 * * *")
    public void updateCallStatus() {
        List<CprCall> callList = cprCallRepository.findAllCallInProgressButExpired();

        for(CprCall cprCall : callList){
            cprCall.endSituationCprCall();
            cprCallRepository.save(cprCall);
        }
    }

    private void sendExpiredFcm(User angel) {
        try {
            firebaseCloudMessageService.sendMessageTo(
                    angel.getDeviceToken().getToken(),
                    ANGEL_EXPIRED_TITLE.getMessage(),
                    ANGEL_EXPIRED_BODY.getMessage(),
                    new LinkedHashMap<String, String>(){{
                        put("type", String.valueOf(FcmPushType.ANGLE_EXPIRATION.ordinal()));
                    }});
        } catch (IOException e) {
            throw new CustomException(SERVER_ERROR_FAILED_TO_SEND_FCM);
        }
    }
}
