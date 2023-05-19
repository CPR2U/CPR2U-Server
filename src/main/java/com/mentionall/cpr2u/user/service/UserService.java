package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.progress.EducationProgress;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.mentionall.cpr2u.util.exception.ResponseCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final TwilioUtil twilioUtil;

    public TokenResponseDto signup(SignUpRequestDto requestDto) {
        User user = new User(requestDto);
        userRepository.save(user);

        createDeviceToken(requestDto.getDeviceToken(), user);
        createEducationProgress(user);

        return issueUserTokens(user);
    }

    public CodeResponseDto getVerificationCode(PhoneNumberRequestDto requestDto) {
        String code = String.format("%04.0f", Math.random() * Math.pow(10, 4));
        twilioUtil.sendSMS(requestDto.getPhoneNumber(), "Your verification code is " + code);

        return new CodeResponseDto(code);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByPhoneNumber(requestDto.getPhoneNumber())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        createDeviceToken(requestDto.getDeviceToken(), user);

        return issueUserTokens(user);
    }

    @Transactional
    public TokenResponseDto reissueToken(TokenReissueRequestDto requestDto) {
        if (!jwtTokenProvider.validateToken(requestDto.getRefreshToken()))
            throw new CustomException(FORBIDDEN_TOKEN_NOT_VALID);

        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByToken(requestDto.getRefreshToken())
                .orElseThrow(()-> new CustomException(FORBIDDEN_TOKEN_NOT_VALID));
        return issueUserTokens(refreshToken.getUser());
    }

    public void checkNicknameDuplicated(String nickname) {
        if(userRepository.existsByNickname(nickname))
            throw new CustomException(BAD_REQUEST_NICKNAME_DUPLICATED);
    }

    public void certificate(User user, LocalDateTime dateTime) {
        user.acquireCertification(dateTime);
        userRepository.save(user);
    }

    private TokenResponseDto issueUserTokens(User user){
        return new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user),
                createRefreshToken(user).getToken());
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new RefreshToken(user));

        refreshToken.setToken(jwtTokenProvider.createRefreshToken(user));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private void createDeviceToken(String token, User user) {
        DeviceToken deviceToken = deviceTokenRepository.findByUserId(user.getId())
                .orElseGet(() -> new DeviceToken(user));

        deviceToken.setToken(token);
        deviceTokenRepository.save(deviceToken);

        user.setDeviceToken(deviceToken);
        userRepository.save(user);
    }

    private void createEducationProgress(User user) {
        EducationProgress progress = new EducationProgress(user);
        progressRepository.save(progress);

        user.setEducationProgress(progress);
        userRepository.save(user);
    }
}
