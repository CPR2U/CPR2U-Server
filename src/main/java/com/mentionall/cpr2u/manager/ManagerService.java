package com.mentionall.cpr2u.manager;

import com.mentionall.cpr2u.call.dto.FcmPushTypeEnum;
import com.mentionall.cpr2u.call.service.FirebaseCloudMessageService;
import com.mentionall.cpr2u.user.domain.AngelStatusEnum;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.MessageEnum;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Scheduled(cron = "1 0 0 * * *")
    public void updateAngelStatus() {
        List<User> userList = userRepository.findAllAngel();
        for (User user : userList) {
            int leftDays = 90 + (int) (ChronoUnit.DAYS.between(LocalDate.now(), user.getDateOfIssue().toLocalDate().atStartOfDay()));
            System.out.println(user.getNickname() + " 남은 기간: " + leftDays);
            if (leftDays < 0) {
                user.expireCertificate();
                userRepository.save(user);
            }
            else if (leftDays == 7) {
                try {
                    firebaseCloudMessageService.sendMessageTo(user.getDeviceToken().getToken(),
                            MessageEnum.ANGEL_EXPIRATION_BEFORE_7_TITLE.getMessage(),
                            MessageEnum.ANGEL_EXPIRATION_BEFORE_7_BODY.getMessage(),
                            FcmPushTypeEnum.ANGLE_EXPIRATION.ordinal());
                } catch (IOException e) {
                    throw new CustomException(ResponseCode.SERVER_ERROR_FAILED_TO_SEND_FCM);

                }
            }
        }
    }
}
