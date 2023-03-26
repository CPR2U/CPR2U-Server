package com.mentionall.cpr2u.call.controller;

import com.mentionall.cpr2u.call.dto.CprCallIdResponseDto;
import com.mentionall.cpr2u.call.dto.CprCallNearUserResponseDto;
import com.mentionall.cpr2u.call.dto.CprCallRequestDto;
import com.mentionall.cpr2u.call.service.CprCallService;
import com.mentionall.cpr2u.user.domain.PrincipalDetails;
import com.mentionall.cpr2u.util.GetUserDetails;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CallController", description = "호출 화면 컨트롤러")
@RequestMapping("/call")
@RequiredArgsConstructor
@RestController
public class CprCallController {

    private final CprCallService cprCallService;

    @Operation(summary = "홈 화면", description = "현재 근처에서 진행중인 CPR 요청이 있는지 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CprCallNearUserResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "엔젤 자격증이 있으나, 주소를 설정하지 않음", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class))))
    })
    @GetMapping
    public ResponseEntity<ResponseDataTemplate> getNowCallStatusNearUser(@GetUserDetails PrincipalDetails userDetails) {
        return ResponseDataTemplate.toResponseEntity(ResponseCode.OK, cprCallService.getCallNearUser(userDetails.getUser()));
    }

    @Operation(summary = "호출하기", description = "사건 발생 지역의 CPR Angel들을 호출한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CprCallIdResponseDto.class))))
    })
    @PostMapping
    public ResponseEntity<ResponseDataTemplate> makeCall(@RequestBody CprCallRequestDto cprCallRequestDto,
                                                         @GetUserDetails PrincipalDetails userDetails) {
        return ResponseDataTemplate.toResponseEntity(ResponseCode.OK, cprCallService.makeCall(cprCallRequestDto, userDetails.getUser()));
    }

    @Operation(summary = "호출 상황 종료", description = "호출을 중단한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class)))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 호출 데이터가 없습니다.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class)))),
    })
    @PostMapping("/end/{call_id}")
    public ResponseEntity<ResponseTemplate> endCall(@PathVariable(name="call_id") Long callId) {
        cprCallService.endCall(callId);
        return ResponseTemplate.toResponseEntity(ResponseCode.OK_CPR_CALL_END_SITUDATION);
    }
}
