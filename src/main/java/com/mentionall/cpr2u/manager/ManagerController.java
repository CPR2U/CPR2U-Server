package com.mentionall.cpr2u.manager;

import com.mentionall.cpr2u.education.dto.quiz.OXQuizRequestDto;
import com.mentionall.cpr2u.education.dto.quiz.SelectionQuizRequestDto;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
@Hidden
public class ManagerController {

    private final QuizService quizService;

    @PostMapping("/quizzes/ox")
    public ResponseEntity<ResponseTemplate> createOxQuiz(@RequestBody OXQuizRequestDto requestDto) {
        quizService.createOXQuiz(requestDto);
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @PostMapping("/quizzes/selection")
    public ResponseEntity<ResponseTemplate> createSelectionQuiz(@RequestBody SelectionQuizRequestDto requestDto) {
        quizService.createSelectionQuiz(requestDto);
        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }
}
