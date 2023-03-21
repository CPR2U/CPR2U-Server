package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.education.domain.EducationProgress;
import com.mentionall.cpr2u.education.repository.EducationProgressRepository;
import com.mentionall.cpr2u.user.domain.DeviceToken;
import com.mentionall.cpr2u.user.domain.RefreshToken;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.*;
import com.mentionall.cpr2u.user.repository.DeviceTokenRepository;
import com.mentionall.cpr2u.user.repository.RefreshTokenRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.util.exception.CustomException;
import com.mentionall.cpr2u.util.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    public UserTokenDto signup(UserSignUpDto userSignUpDto) {
        User user = new User(userSignUpDto);
        userRepository.save(user);
        deviceTokenRepository.save(new DeviceToken(userSignUpDto.getDeviceToken(), user));
        progressRepository.save(new EducationProgress(user));
        return issueUserToken(user);
    }

    public UserCodeDto getVerificationCode(UserPhoneNumberDto userPhoneNumberDto) {
        return new UserCodeDto(String.format("%04.0f", Math.random() * Math.pow(10, 4)));
    }

    @Transactional
    public UserTokenDto login(UserLoginDto userLoginDto) {
        if(userRepository.existsByPhoneNumber(userLoginDto.getPhoneNumber())){
            User user = userRepository.findByPhoneNumber(userLoginDto.getPhoneNumber())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

            DeviceToken deviceToken = deviceTokenRepository.findByUserId(user.getId())
                    .orElse(new DeviceToken(userLoginDto.getDeviceToken(), user));
            if(!deviceToken.getDeviceToken().equals(userLoginDto.getDeviceToken())) {
                deviceToken.setDeviceToken(userLoginDto.getDeviceToken());
                deviceTokenRepository.save(deviceToken);
                user.setDeviceToken(deviceToken);
                userRepository.save(user);
            }

            return issueUserToken(user);
        }
        throw new CustomException(ResponseCode.NOT_FOUND_USER);
    }

    public UserTokenDto reissueToken(UserTokenReissueDto userTokenReissueDto) {
        RefreshToken refreshToken;
        if(jwtTokenProvider.validateToken(userTokenReissueDto.getRefreshToken()))
            refreshToken = refreshTokenRepository.findByRefreshToken(userTokenReissueDto.getRefreshToken())
                    .orElseThrow(()-> new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID));
        else throw new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID);

        User user = refreshToken.getUser();
        return issueUserToken(user);
    }

    public UserTokenDto issueUserToken(User user){
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(new RefreshToken(user));
        refreshToken.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return new UserTokenDto(
                jwtTokenProvider.createToken(user.getId(), user.getRoles()),
                newRefreshToken);
    }

    public void checkNicknameDuplicated(String nickname) {
        System.out.println(nickname);
        System.out.println(userRepository.existsByNickname(nickname));
        if(userRepository.existsByNickname(nickname))
            throw new CustomException(ResponseCode.BAD_REQUEST_NICKNAME_DUPLICATED);
    }

    public void certificate(User user) {
        user.acquireCertification();
    }
}
