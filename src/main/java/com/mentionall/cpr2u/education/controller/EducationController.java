package com.mentionall.cpr2u.education.controller;

import com.mentionall.cpr2u.education.service.EducationProgressService;
import com.mentionall.cpr2u.education.service.LectureService;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
public class EducationController {
    private final EducationProgressService progressService;
    private final LectureService lectureService;
    private final QuizService quizService;

    @GetMapping()
    public ResponseEntity<ResponseTemplate> getEducationInfo() {
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/lecture")
    public ResponseEntity<ResponseTemplate> getLectureList() {
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @GetMapping("/lecture/progress/{lectureId}")
    public ResponseEntity<ResponseTemplate> takeLecture(@PathVariable Long lectureId) {
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }
}
