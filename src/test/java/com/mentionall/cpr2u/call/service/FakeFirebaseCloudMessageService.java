package com.mentionall.cpr2u.call.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class FakeFirebaseCloudMessageService extends FirebaseCloudMessageService{
    public FakeFirebaseCloudMessageService() {
        super(null);
    }

    @Override
    public void sendMessageTo(String targetToken, String title, String body, Map<String, String> data) {

    }
}
