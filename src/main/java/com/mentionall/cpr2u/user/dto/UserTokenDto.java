package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mentionall.cpr2u.user.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@AllArgsConstructor
public class UserTokenDto {

    @Schema(example = "access token")
    @JsonProperty("access_token")
    String accessToken;

    @Schema(example = "refresh token")
    @JsonProperty("refresh_token")
    String refreshToken;

}
