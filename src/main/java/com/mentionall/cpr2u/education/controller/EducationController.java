package com.mentionall.cpr2u.education.controller;

import com.mentionall.cpr2u.education.dto.*;
import com.mentionall.cpr2u.education.service.EducationProgressService;
import com.mentionall.cpr2u.education.service.LectureService;
import com.mentionall.cpr2u.education.service.QuizService;
import com.mentionall.cpr2u.util.ResponseDataTemplate;
import com.mentionall.cpr2u.util.ResponseTemplate;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "EducationController", description = "학습 화면 컨트롤러")
@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
public class  EducationController {
    private final EducationProgressService progressService;
    private final LectureService lectureService;
    private final QuizService quizService;

    @Operation(summary = "유저의 학습 화면 정보 조회", description = "유저의 엔젤 자격과 현재 학습 진도 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EducationProgressDto.class)))),
    })
    @GetMapping()
    public ResponseEntity<ResponseDataTemplate> getEducationInfo(HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();

        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                progressService.readEducationInfo(userId));
    }

    @Operation(summary = "유저의 강의 리스트 조회", description = "강의 리스트와 유저의 현재 강의 진도를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LectureProgressDto.class)))),
    })
    @GetMapping("/lecture")
    public ResponseEntity<ResponseDataTemplate> getLectureList(HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();

        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                lectureService.readLectureProgressList(userId));
    }

    @Operation(summary = "강의 수강 완료", description = "유저가 마지막으로 완료한 강의를 lectureId 값의 강의로 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class)))),
    })
    @PostMapping("/lecture/progress/{lectureId}")
    public ResponseEntity<ResponseTemplate> completeLecture(
            @Parameter(description = "완강한 강의 ID") @PathVariable Long lectureId,
            HttpServletRequest request) {
        String userId = request.getUserPrincipal().getName();
        progressService.completeLecture(userId, lectureId);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @Operation(summary = "퀴즈 질문 조회", description = "5개의 퀴즈 질문과 답변 리스트를 랜덤으로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuizDto.class)))),
    })
    @GetMapping("/quiz")
    public ResponseEntity<ResponseDataTemplate> getQuizList() {
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                quizService.readRandom5Quiz());
    }

    @Operation(summary = "퀴즈 테스트 완료", description = "유저가 퀴즈 테스트를 통과했음을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class)))),
    })
    @PostMapping("/quiz/progress")
    public ResponseEntity<ResponseTemplate> completeQuiz(
            HttpServletRequest request,
            @Parameter(description = "유저의 퀴즈 점수") @RequestBody ScoreDto requestDto
            ) {
        String userId = request.getUserPrincipal().getName();
        progressService.completeQuiz(userId, requestDto);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }

    @Operation(summary = "자세실습 강의 조회", description = "자세실습 강의 영상 URL를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LectureResponseDto.class)))),
    })
    @GetMapping("/posture")
    public ResponseEntity<ResponseDataTemplate> getPostureLecture() {
        return ResponseDataTemplate.toResponseEntity(
                ResponseCode.OK,
                lectureService.readPostureLecture());
    }

    @Operation(summary = "자세실습 테스트 완료", description = "유저가 자세실습 테스트를 통과했음을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseTemplate.class)))),
    })
    @PostMapping("/posture/progress")
    public ResponseEntity<ResponseTemplate> completePosture(
            HttpServletRequest request,
            @Parameter(description = "유저의 자세실습 점수") @RequestBody ScoreDto requestDto) {
        String userId = request.getUserPrincipal().getName();
        progressService.completePosture(userId, requestDto);

        return ResponseTemplate.toResponseEntity(ResponseCode.OK);
    }


}
