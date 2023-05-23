package com.mentionall.cpr2u.call.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
    public static FirebaseApp firebaseApp;
    public static String firebaseAppName = "CPR2U";

    @PostConstruct
    private void initialFirebaseApp() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                .build();

        firebaseApp = FirebaseApp.initializeApp(options, firebaseAppName);

    }

    public void sendFcmMessage(List<String> deviceTokenToSendList, String title, String body, Map<String, String> data) {

        if (deviceTokenToSendList.size() > 0) {
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(new Notification(title, body))
                    .addAllTokens(deviceTokenToSendList)
                    .putAllData(data).build();

            try {
                FirebaseMessaging.getInstance(firebaseApp).sendMulticast(message);
            } catch (FirebaseMessagingException e) {
                throw new CustomException(ResponseCode.SERVER_ERROR_FAILED_TO_SEND_FCM);
            }
        }
    }

}