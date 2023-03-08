package com.mentionall.cpr2u.user.controller;

import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "UserController", description = "회원 정보 관리")
public class UserController {
    @GetMapping("/test")
    public ResponseEntity<ResponseDataTemplate> autoLogin(HttpServletRequest httpServletRequest){
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                httpServletRequest.getUserPrincipal().getName()
        );
    }
}
