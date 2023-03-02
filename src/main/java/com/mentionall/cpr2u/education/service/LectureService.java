package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.dto.LectureDto;
import com.mentionall.cpr2u.education.dto.LectureProgressDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final EducationProgressRepository progressRepository;

    public LectureProgressDto readLectureProgressList(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );

        List<LectureDto> lectureDtoList = new ArrayList();
        lectureRepository.findAll().stream()
                .map(l -> lectureDtoList.add(new LectureDto(l)));

        return new LectureProgressDto(progress, lectureDtoList);
    }

    public LectureDto readPostureLecture() {
        Lecture postureLecture = lectureRepository.findByStep(5).orElseThrow(
                () -> new CustomException(ResponseCode.LECTURE_NOT_FOUND)
        );
        return new LectureDto(postureLecture);
    }
}
