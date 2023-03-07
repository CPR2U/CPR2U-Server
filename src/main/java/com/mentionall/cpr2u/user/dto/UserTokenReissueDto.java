package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserTokenReissueDto {

    @Schema(example = "refresh token")
    @JsonProperty("refresh_token")
    String refreshToken;

}
