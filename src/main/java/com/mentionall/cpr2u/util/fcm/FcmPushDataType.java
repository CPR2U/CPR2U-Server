package com.mentionall.cpr2u.util.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FcmPushDataType {

    TYPE("type"),
    CPR_CALL_ID("call");

    private final String type;
}
