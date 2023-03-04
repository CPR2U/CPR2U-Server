package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.ProgressStatus;
import com.mentionall.cpr2u.education.dto.LectureResponseDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EducationProgressTest {
    @Autowired
    private LectureService lectureService;

    @Autowired
    private EducationProgressService progressService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void readTotalEducationProgress() {

    }

    @Test
    @Transactional
    public void completeLecture() {
        //given
        UserSignUpDto signUpDto = new UserSignUpDto("현애", "010-0000-0000");
        String accessToken = userService.signup(signUpDto).getAccessToken();
        String userId = jwtTokenProvider.getUserId(accessToken);

        //when & then
        List<LectureResponseDto> lectureList = lectureService.readLectureProgressList(userId).getLectureList();

        ProgressStatus lectureStatus = progressService.readEducationInfo(userId).getIsLectureCompleted();
        assertThat(lectureStatus).isEqualTo(ProgressStatus.NotCompleted);

        for (LectureResponseDto lecture : lectureList) {
            progressService.completeLecture(userId, lecture.getId());

            int lastStep = lectureService.readLectureProgressList(userId).getLastStep();
            assertThat(lastStep).isEqualTo(lecture.getStep());

            lectureStatus = progressService.readEducationInfo(userId).getIsLectureCompleted();
            assertThat(lectureStatus).isEqualTo(ProgressStatus.InProgress);
        }

        lectureStatus = progressService.readEducationInfo(userId).getIsLectureCompleted();
        assertThat(lectureStatus).isEqualTo(ProgressStatus.Completed);
    }

    @Test
    public void completeQuiz() {

    }

    @Test
    public void readPostureLecture() {

    }

    @Test
    public void completePosture() {

    }
}
