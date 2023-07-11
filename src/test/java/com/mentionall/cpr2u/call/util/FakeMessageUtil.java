package com.mentionall.cpr2u.call.util;

import com.mentionall.cpr2u.util.fcm.MessageUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FakeMessageUtil implements MessageUtil {

    @Override
    public void sendMessage(List<String> deviceTokenToSendList, String title, String body, Map<String, String> data) {

    }
}
