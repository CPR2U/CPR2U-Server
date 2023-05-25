package com.mentionall.cpr2u;

import com.mentionall.cpr2u.call.service.FakeFirebaseCloudMessageUtil;
import com.mentionall.cpr2u.call.service.FirebaseCloudMessageUtil;
import com.mentionall.cpr2u.util.twilio.FakeTwilioUtil;
import com.mentionall.cpr2u.util.twilio.TwilioUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public FirebaseCloudMessageUtil firebaseCloudMessageService() {
        return new FakeFirebaseCloudMessageUtil();
    }

    @Bean
    public TwilioUtil twilioUtil(){
        return new FakeTwilioUtil();
    }
}
