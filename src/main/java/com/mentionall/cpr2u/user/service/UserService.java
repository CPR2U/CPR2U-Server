package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.UserJwtDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;

    public UserJwtDto signup(UserSignUpDto userSignUpDto) {

        User user = new User(userSignUpDto);

        return new UserJwtDto(
                jwtTokenProvider.createToken(user.getId()),
                jwtTokenProvider.createToken(user.getId()));
    }
}
