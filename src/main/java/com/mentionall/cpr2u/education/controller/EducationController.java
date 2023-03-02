package com.mentionall.cpr2u.education.controller;

import com.mentionall.cpr2u.education.service.EducationProgressService;
import com.mentionall.cpr2u.education.service.LectureService;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
public class  EducationController {
    private final EducationProgressService progressService;
    private final LectureService lectureService;
    private final QuizService quizService;

    @GetMapping()
    public ResponseEntity<ResponseTemplate> getEducationInfo() {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/lecture")
    public ResponseEntity<ResponseTemplate> getLectureList() {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @PostMapping("/lecture/progress/{lectureId}")
    public ResponseEntity<ResponseTemplate> completeLecture(@PathVariable Long lectureId) {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/quiz")
    public ResponseEntity<ResponseDataTemplate> getQuizList() {
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                quizService.readRandomQuizList(5));
    }

    @PostMapping("/quiz/progress")
    public ResponseEntity<ResponseTemplate> completeQuiz() {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/posture")
    public ResponseEntity<ResponseTemplate> getPostureLecture() {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @PostMapping("/posture/progress")
    public ResponseEntity<ResponseTemplate> completePosture() {
        // TODO: 비즈니스 로직 구현

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }
}
