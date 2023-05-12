package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.user.domain.DeviceToken;
import com.mentionall.cpr2u.user.domain.RefreshToken;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.user.*;
import com.mentionall.cpr2u.user.repository.device_token.DeviceTokenRepository;
import com.mentionall.cpr2u.user.repository.RefreshTokenRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.TwilioUtil;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final TwilioUtil twilioUtil;

    public UserTokenDto signup(UserSignUpDto requestDto) {
        User user = new User(requestDto);

        userRepository.save(user);
        deviceTokenRepository.save(new DeviceToken(requestDto.getDeviceToken(), user));
        progressRepository.save(new EducationProgress(user));

        return issueUserTokens(user);
    }

    public UserCodeDto getVerificationCode(UserPhoneNumberDto requestDto) {
        String code = String.format("%04.0f", Math.random() * Math.pow(10, 4));

        twilioUtil.sendSMS(requestDto.getPhoneNumber(), "Your verification code is " + code);

        return new UserCodeDto(code);
    }

    @Transactional
    public UserTokenDto login(UserLoginDto requestDto) {
        User user = userRepository.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

        DeviceToken deviceToken = deviceTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new DeviceToken(user));

        deviceToken.setToken(requestDto.getDeviceToken());
        deviceTokenRepository.save(deviceToken);

        user.setDeviceToken(deviceToken);
        userRepository.save(user);

        return issueUserTokens(user);
    }

    @Transactional
    public UserTokenDto reissueToken(UserTokenReissueDto userTokenReissueDto) {
        RefreshToken refreshToken;
        if(jwtTokenProvider.validateToken(userTokenReissueDto.getRefreshToken()))
            refreshToken = refreshTokenRepository.findRefreshTokenByToken(userTokenReissueDto.getRefreshToken())
                    .orElseThrow(()-> new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID));
        else throw new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID);

        User user = refreshToken.getUser();
        return issueUserTokens(user);
    }

    private UserTokenDto issueUserTokens(User user){
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new RefreshToken(user));

        refreshToken.setToken(jwtTokenProvider.createRefreshToken(user));
        refreshTokenRepository.save(refreshToken);

        return new UserTokenDto(
                jwtTokenProvider.createAccessToken(user),
                refreshToken.getToken());
    }

    public void checkNicknameDuplicated(String nickname) {
        if(userRepository.existsByNickname(nickname))
            throw new CustomException(ResponseCode.BAD_REQUEST_NICKNAME_DUPLICATED);
    }

    public void certificate(User user, LocalDateTime dateTime) {
        user.acquireCertification(dateTime);
        userRepository.save(user);
    }
}
