package com.mentionall.cpr2u.util.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(responseCode.getHttpStatus().value())
                        .code(responseCode.name())
                        .message(responseCode.getDetail())
                        .build()
                );
    }
}