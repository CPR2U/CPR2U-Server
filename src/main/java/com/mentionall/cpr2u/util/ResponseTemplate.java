package com.mentionall.cpr2u.util;

import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class ResponseTemplate {
    public int status;
    public String message;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public static ResponseEntity<ResponseTemplate> toResponseEntity(ResponseCode responseCode) {
        return ResponseEntity
                .status(responseCode.getHttpStatus())
                .body(ResponseTemplate.builder()
                        .status(responseCode.getHttpStatus().value())
                        .message(responseCode.getDetail())
                        .build()
                );
    }
}