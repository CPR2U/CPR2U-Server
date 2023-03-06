package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.domain.Lecture;
import com.mentionall.cpr2u.education.domain.LectureType;
import com.mentionall.cpr2u.education.dto.LectureRequestDto;
import com.mentionall.cpr2u.education.dto.LectureResponseDto;
import com.mentionall.cpr2u.education.dto.LectureProgressDto;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.education.repository.LectureRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final EducationProgressRepository progressRepository;

    public void createLecture(LectureRequestDto requestDto) {
        if (lectureRepository.existsByStep(requestDto.getStep()))
            throw new CustomException(ResponseCode.LECTURE_STEP_DUPLICATED);

        lectureRepository.save(new Lecture(requestDto));
    }

    public LectureProgressDto readLectureProgress(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION)
        );
        EducationProgress progress = progressRepository.findByUser(user).orElseThrow(
                () -> new CustomException(ResponseCode.EDUCATION_PROGRESS_NOT_FOUND)
        );

        List<LectureResponseDto> lectureResponseDtoList = lectureRepository.findAllByType(LectureType.THEORY)
                .stream().sorted()
                .map(l -> new LectureResponseDto(l))
                .collect(Collectors.toList());
        return new LectureProgressDto(progress, lectureResponseDtoList);
    }

    public List<LectureResponseDto> readAllTheoryLecture() {
        return lectureRepository.findAllByType(LectureType.THEORY).stream().sorted()
                .map(l -> new LectureResponseDto(l))
                .collect(Collectors.toList());
    }

    public List<LectureResponseDto> readPostureLecture() {
        return lectureRepository.findAllByType(LectureType.POSTURE).stream()
                .map(l -> new LectureResponseDto(l))
                .collect(Collectors.toList());
    }
}
