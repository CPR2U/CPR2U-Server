package com.mentionall.cpr2u.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    @Schema(example = "시/도")
    private String sido;

    @JsonProperty("gugun_list")
    private List<SigugunResponseDto> gugunList = new ArrayList();
}