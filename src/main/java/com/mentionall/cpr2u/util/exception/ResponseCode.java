package com.mentionall.cpr2u.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(HttpStatus.OK, "성공"),
    LECTURE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "자세 연습 강의가 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
