package com.mentionall.cpr2u.util.fcm;

import java.util.List;
import java.util.Map;

public interface MessageUtil {
    void sendMessage(List<String> deviceTokenToSendList, String title, String body, Map<String, String> data);
}
