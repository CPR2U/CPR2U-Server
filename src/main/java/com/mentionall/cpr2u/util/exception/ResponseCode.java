package com.mentionall.cpr2u.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK, "성공"),
    LECTURE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 강의가 없습니다."),
    EDUCATION_PROGRESS_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 교육 진도 정보가 조회되지 않습니다."),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "회원 정보 없음");

    private final HttpStatus httpStatus;
    private final String detail;
}
