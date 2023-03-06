package com.mentionall.cpr2u.user.controller;

import com.mentionall.cpr2u.user.dto.UserCodeDto;
import com.mentionall.cpr2u.user.dto.UserJwtDto;
import com.mentionall.cpr2u.user.dto.UserLoginDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.service.UserService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "회원가입/로그인")
public class AuthController {
    private final UserService userService;

    @Operation(summary = "회원가입",
            method = "POST",
            description = "회원가입 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserJwtDto.class))))
    })

    @PostMapping("/signup")
    public ResponseEntity<ResponseDataTemplate> signup(@RequestBody UserSignUpDto userSignUpDto){
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                userService.signup(userSignUpDto)
        );
    }

    @Operation(summary = "로그인 인증번호 발급",
            method = "GET",
            description = "로그인 인증번호 발급 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 발급 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserCodeDto.class))))
    })

    @GetMapping("/login")
    public ResponseEntity<ResponseDataTemplate> login(){
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                userService.getVerificationCode()
        );
    }

    @Operation(summary = "인증된 사용자 로그인 처리",
            method = "POST",
            description = "인증된 사용자 로그인 처리 API"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 확인 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserJwtDto.class))))
    })

    @GetMapping("/verification")
    public ResponseEntity<ResponseDataTemplate> verificationUserLogin(@RequestBody UserLoginDto userLoginDto){
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                userService.login(userLoginDto)
        );
    }
}
