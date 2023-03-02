package com.mentionall.cpr2u.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "성공");

    private final HttpStatus httpStatus;
    private final String detail;
}
