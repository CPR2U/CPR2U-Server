package com.mentionall.cpr2u.call.controller;

import com.mentionall.cpr2u.call.dto.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.ReportRequestDto;
import com.mentionall.cpr2u.call.service.DispatchService;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/dispatch")
@RestController
@RequiredArgsConstructor
public class DispatchController {
    private final DispatchService dispatchService;

    @PostMapping
    public ResponseEntity<ResponseDataTemplate> dispatch(
            @RequestBody DispatchRequestDto requestDto,
            HttpServletRequest request) {
        var userId = request.getUserPrincipal().getName();
        return ResponseDataTemplate.toResponseEntity(ResponseCode.OK, dispatchService.dispatch(userId, requestDto));
    }

    @PostMapping("/arrive/{dispatch_id}")
    public ResponseEntity<ResponseTemplate> arrive(@PathVariable(value = "dispatch_id") Long dispatchId) {
        dispatchService.arrive(dispatchId);
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @PostMapping("/report")
    public ResponseEntity<ResponseTemplate> report(@RequestBody ReportRequestDto requestDto) {
        dispatchService.report(requestDto);
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }
}
