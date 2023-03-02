package com.mentionall.cpr2u.education.service;

import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationProgressService {
    private final EducationProgressRepository progressRepository;
}
