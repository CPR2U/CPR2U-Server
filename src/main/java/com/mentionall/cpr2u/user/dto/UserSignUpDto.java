package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserSignUpDto {

    @Schema(example = "사용자 이름")
    @JsonProperty("nick_name")
    String nickName;

    @Schema(example = "사용자 전화번호")
    @JsonProperty("phone_number")
    String phoneNumber;
}
