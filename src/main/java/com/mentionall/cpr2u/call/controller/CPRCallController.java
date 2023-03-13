package com.mentionall.cpr2u.call.controller;

import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.service.CprCallService;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
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

import javax.servlet.http.HttpServletRequest;

@Tag(name = "CallController", description = "호출 화면 컨트롤러")
@RequestMapping("/call")
@RequiredArgsConstructor
@RestController
public class CPRCallController {

    private final CprCallService CPRCallService;

    @Operation(summary = "홈 화면", description = "현재 근처에서 진행중인 CPR 요청이 있는지 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DispatchResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<ResponseDataTemplate> getNowCallStatusNearUser(HttpServletRequest request) {
        var userId = request.getUserPrincipal().getName();
        return ResponseDataTemplate.toResponseEntity(ResponseCode.OK, CPRCallService.getCallNearUser(userId));
    }
}
