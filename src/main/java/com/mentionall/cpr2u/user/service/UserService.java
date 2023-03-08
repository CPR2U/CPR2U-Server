package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.*;
import com.mentionall.cpr2u.user.repository.RefreshTokenRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final EducationProgressRepository progressRepository;

    public UserTokenDto signup(UserSignUpDto userSignUpDto) {

        User user = new User(userSignUpDto);
        userRepository.save(user);
        progressRepository.save(new EducationProgress(user));
        return issueUserToken(user);
    }

    public UserCodeDto getVerificationCode() {
        return new UserCodeDto(String.format("%04.0f", Math.random() * Math.pow(10, 4)));
    }

    public UserTokenDto login(UserLoginDto userLoginDto) {

        if(userRepository.existsByPhoneNumber(userLoginDto.getPhoneNumber())){
            User user = userRepository.findByPhoneNumber(userLoginDto.getPhoneNumber())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION));
            return issueUserToken(user);
        }
        else throw new CustomException(ResponseCode.NOT_FOUND_USER_EXCEPTION);
    }

    public UserTokenDto reissueToken(UserTokenReissueDto userTokenReissueDto) {
        RefreshToken refreshToken;
        if(jwtTokenProvider.validateToken(userTokenReissueDto.getRefreshToken()))
            refreshToken = refreshTokenRepository.findByRefreshToken(userTokenReissueDto.getRefreshToken())
                    .orElseThrow(()-> new CustomException(ResponseCode.NOT_FOUND_REFRESH_TOKEN));
        else throw new CustomException(ResponseCode.FORBIDDEN_NOT_VALID_TOKEN);

        User user = refreshToken.getUser();
        return issueUserToken(user);
    }

    public UserTokenDto issueUserToken(User user){

        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(null);
        if(refreshToken != null)
            refreshToken.setRefreshToken(newRefreshToken);
        else refreshToken = new RefreshToken(newRefreshToken, user);
        refreshTokenRepository.save(refreshToken);

        return new UserTokenDto(
                user.getRoles(),
                jwtTokenProvider.createToken(user.getId(), user.getRoles()),
                newRefreshToken);
    }
}
