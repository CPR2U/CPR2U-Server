package com.mentionall.cpr2u.manager;

import com.mentionall.cpr2u.education.dto.quiz.QuizRequestDto;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mentionall.cpr2u.util.exception.ResponseCode.*;

@Slf4j
@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
@Hidden
public class ManagerController {

    private final QuizService quizService;

    @PostMapping("/quizzes")
    public ResponseEntity<ResponseTemplate> createQuiz(@RequestBody QuizRequestDto requestDto) {
        quizService.createQuiz(requestDto);
        return ResponseTemplate.toResponseEntity(OK_SUCCESS);
    }

}
