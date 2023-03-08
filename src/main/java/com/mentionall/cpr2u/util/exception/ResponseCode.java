package com.mentionall.cpr2u.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(HttpStatus.OK, "성공"),
    LECTURE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "해당 강의를 찾을 수 없습니다."),
    LECTURE_STEP_DUPLICATED(HttpStatus.BAD_REQUEST, "중북되는 섹션의 강의가 존재합니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 퀴즈를 찾을 수 없습니다."),
    QUIZ_BAD_REQUEST(HttpStatus.BAD_REQUEST, "해당 퀴즈의 답안 인덱스가 잘못된 값을 갖고 있습니다."),
    EDUCATION_PROGRESS_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 교육 진도 정보가 조회되지 않습니다."),
    EDUCATION_PROGRESS_BAD_REQUEST(HttpStatus.BAD_REQUEST, "이전 진도를 모두 완료해야 수강할 수 있습니다."),
    EDUCATION_QUIZ_FAIL(HttpStatus.BAD_REQUEST, "점수가 낮아 퀴즈 테스트를 실패했습니다."),
    EDUCATION_POSTURE_FAIL(HttpStatus.BAD_REQUEST, "점수가 낮아 자세실습 테스트를 실패했습니다."),
    NOT_FOUND_USER_EXCEPTION(HttpStatus.NOT_FOUND, "회원 정보 없음");

    private final HttpStatus httpStatus;
    private final String detail;
}
