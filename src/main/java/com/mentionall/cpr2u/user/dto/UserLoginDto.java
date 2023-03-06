package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserLoginDto {

    @Schema(example = "전화번호")
    @JsonProperty("phone_number")
    String phoneNumber;

    @Schema(example = "디바이스 토큰")
    @JsonProperty("device_token")
    String deviceToken;
}
