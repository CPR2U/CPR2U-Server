package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
}
