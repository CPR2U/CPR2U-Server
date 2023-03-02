package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserJwtDto {

    @Schema(example = "access token")
    @JsonProperty("access_token")
    String accessToken;

    @Schema(example = "refresh token")
    @JsonProperty("refresh_token")
    String refreshToken;
}
