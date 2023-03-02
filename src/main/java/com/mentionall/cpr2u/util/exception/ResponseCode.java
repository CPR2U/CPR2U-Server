package com.mentionall.cpr2u.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK, "성공"),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "회원 정보 없음");

    private final HttpStatus httpStatus;
    private final String detail;
}
