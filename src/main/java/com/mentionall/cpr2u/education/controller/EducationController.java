package com.mentionall.cpr2u.education.controller;

import com.mentionall.cpr2u.education.dto.PostureRequestDto;
import com.mentionall.cpr2u.education.dto.QuizRequestDto;
import com.mentionall.cpr2u.education.service.EducationProgressService;
import com.mentionall.cpr2u.education.service.LectureService;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
public class  EducationController {
    private final EducationProgressService progressService;
    private final LectureService lectureService;
    private final QuizService quizService;

    @GetMapping()
    public ResponseEntity<ResponseDataTemplate> getEducationInfo(HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();

        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                progressService.readEducationInfo(userId));
    }

    @GetMapping("/lecture")
    public ResponseEntity<ResponseDataTemplate> getLectureList(HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();

        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                lectureService.readLectureProgressList(userId));
    }

    @PostMapping("/lecture/progress/{lectureId}")
    public ResponseEntity<ResponseTemplate> completeLecture(
            @PathVariable Long lectureId,
            HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();
        progressService.completeLecture(userId, lectureId);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/quiz")
    public ResponseEntity<ResponseDataTemplate> getQuizList() {
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                quizService.readRandomQuizList(5));
    }

    @PostMapping("/quiz/progress")
    public ResponseEntity<ResponseTemplate> completeQuiz(
            HttpServletRequest request,
            @RequestBody QuizRequestDto requestDto
            ) {
        String userId = request.getUserPrincipal().getName();
        EducationProgressService.completeQuiz(userId, requestDto);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/posture")
    public ResponseEntity<ResponseDataTemplate> getPostureLecture() {
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                lectureService.readPostureLecture());
    }

    @PostMapping("/posture/progress")
    public ResponseEntity<ResponseTemplate> completePosture(
            HttpServletRequest request,
            @RequestBody PostureRequestDto requestDto) {
        String userId = request.getUserPrincipal().getName();
        EducationProgressService.completePosture(userId, requestDto);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }


}
