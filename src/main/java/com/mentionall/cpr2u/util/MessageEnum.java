package com.mentionall.cpr2u.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageEnum {

    ANGEL_EXPIRATION_BEFORE_7_TITLE("CPR Angel의 만료까지 7일 남았습니다."),
    ANGEL_EXPIRATION_BEFORE_7_BODY("CPR2U에 접속해서 교육 받고 Angel 권한을 연장하세요."),
    CPR_CALL_TITLE("CPR Angel의 출동이 필요합니다.");

    private final String message;

}
