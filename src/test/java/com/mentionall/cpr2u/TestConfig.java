package com.mentionall.cpr2u;

import com.mentionall.cpr2u.call.service.FakeFirebaseCloudMessageService;
import com.mentionall.cpr2u.call.service.FirebaseCloudMessageService;
import com.mentionall.cpr2u.util.twilio.FakeTwilioUtil;
import com.mentionall.cpr2u.util.twilio.TwilioUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public FirebaseCloudMessageService firebaseCloudMessageService() {
        return new FakeFirebaseCloudMessageService();
    }

    @Bean
    public TwilioUtil twilioUtil(){
        return new FakeTwilioUtil();
    }
}
