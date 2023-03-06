package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserCodeDto;
import com.mentionall.cpr2u.user.dto.UserJwtDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserJwtDto signup(UserSignUpDto userSignUpDto) {

        User user = new User(userSignUpDto);
        userRepository.save(user);

        return new UserJwtDto(
                jwtTokenProvider.createToken(user.getId(), user.getRoles()),
                jwtTokenProvider.createRefreshToken());
    }

    public UserCodeDto login() {
        return new UserCodeDto(String.format("%05.0f", Math.random() * Math.pow(10,5)));
    }
}
