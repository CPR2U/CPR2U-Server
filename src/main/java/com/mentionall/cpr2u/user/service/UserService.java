package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserJwtDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;

    public UserJwtDto signup(UserSignUpDto userSignUpDto) {

        User user = new User(userSignUpDto);

        userRepository.save(user);
        progressRepository.save(new EducationProgress(user));

        return new UserJwtDto(
                jwtTokenProvider.createToken(user.getId()),
                jwtTokenProvider.createToken(user.getId()));
    }
}
