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

}
