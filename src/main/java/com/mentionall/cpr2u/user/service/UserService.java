package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.dto.CprCallResponseDto;
import com.mentionall.cpr2u.call.dto.DispatchResponseDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final EducationProgressRepository progressRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final CprCallRepository callRepository;
    private final DispatchRepository dispatchRepository;

    public UserTokenResponseDto signup(UserSignUpRequestDto userSignUpRequestDto) {
        User user = new User(userSignUpRequestDto);
        userRepository.save(user);
        deviceTokenRepository.save(new DeviceToken(userSignUpRequestDto.getDeviceToken(), user));
        progressRepository.save(new EducationProgress(user));
        return issueUserToken(user);
    }

    public UserCodeResponseDto getVerificationCode(UserPhoneNumberRequestDto userPhoneNumberRequestDto) {
        return new UserCodeResponseDto(String.format("%04.0f", Math.random() * Math.pow(10, 4)));
    }

    @Transactional
    public UserTokenResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        if(userRepository.existsByPhoneNumber(userLoginRequestDto.getPhoneNumber())){
            User user = userRepository.findByPhoneNumber(userLoginRequestDto.getPhoneNumber())
                    .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

            DeviceToken deviceToken = deviceTokenRepository.findByUserId(user.getId())
                    .orElse(new DeviceToken(userLoginRequestDto.getDeviceToken(), user));
            if(!deviceToken.getDeviceToken().equals(userLoginRequestDto.getDeviceToken())) {
                deviceToken.setDeviceToken(userLoginRequestDto.getDeviceToken());
                deviceTokenRepository.save(deviceToken);
                user.setDeviceToken(deviceToken);
                userRepository.save(user);
            }

            return issueUserToken(user);
        }
        throw new CustomException(ResponseCode.NOT_FOUND_USER);
    }

    public UserTokenResponseDto reissueToken(UserTokenReissueRequestDto userTokenReissueRequestDto) {
        RefreshToken refreshToken;
        if(jwtTokenProvider.validateToken(userTokenReissueRequestDto.getRefreshToken()))
            refreshToken = refreshTokenRepository.findByRefreshToken(userTokenReissueRequestDto.getRefreshToken())
                    .orElseThrow(()-> new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID));
        else throw new CustomException(ResponseCode.FORBIDDEN_TOKEN_NOT_VALID);

        User user = refreshToken.getUser();
        return issueUserToken(user);
    }

    public UserTokenResponseDto issueUserToken(User user){
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElse(new RefreshToken(user));
        refreshToken.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return new UserTokenResponseDto(
                jwtTokenProvider.createToken(user.getId(), user.getRoles()),
                newRefreshToken);
    }

    public void checkNicknameDuplicated(String nickname) {
        if(userRepository.existsByNickname(nickname))
            throw new CustomException(ResponseCode.BAD_REQUEST_NICKNAME_DUPLICATED);
    }

    public void certificate(User user) {
        user.acquireCertification();
        userRepository.save(user);
    }

    public Page<CprCallResponseDto> getCprCallList(User caller, PageRequest pageRequest) {
        Page<CprCall> callPage = callRepository.findAllByCaller(pageRequest, caller);
    }

    public Page<DispatchResponseDto> getDispatchList(User dispatcher, PageRequest pageRequest) {
        Page<Dispatch> dispatchPage = dispatchRepository.findAllByDispatcher(pageRequest, dispatcher);

    }
}
