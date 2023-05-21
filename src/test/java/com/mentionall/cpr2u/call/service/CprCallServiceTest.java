package com.mentionall.cpr2u.call.service;

import com.mentionall.cpr2u.call.domain.CprCall;
import com.mentionall.cpr2u.call.domain.CprCallStatus;
import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.mentionall.cpr2u.call.dto.cpr_call.CprCallRequestDto;
import com.mentionall.cpr2u.call.dto.dispatch.DispatchRequestDto;
import com.mentionall.cpr2u.call.dto.dispatch.DispatchResponseDto;
import com.mentionall.cpr2u.call.repository.CprCallRepository;
import com.mentionall.cpr2u.call.repository.DispatchRepository;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.address.AddressRequestDto;
import com.mentionall.cpr2u.user.dto.address.SigugunResponseDto;
import com.mentionall.cpr2u.user.dto.user.SignUpRequestDto;
import com.mentionall.cpr2u.user.repository.address.AddressRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.repository.address.AddressRepositoryImpl;
import com.mentionall.cpr2u.user.repository.device_token.DeviceTokenRepository;
import com.mentionall.cpr2u.user.service.AddressService;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("호출 관련 테스트")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CprCallServiceTest {

    private CprCallService cprCallService;
    @Autowired
    private CprCallRepository cprCallRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DispatchService dispatchService;
    @Autowired
    private DispatchRepository dispatchRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    private static final double latitude = 37.56559872345163;
    private static final double longitude = 126.9771473198163;

    public CprCallServiceTest() {

    }

    @BeforeEach
    private void beforeEach() {
        addressService.loadAddressList();
    }

    @Test
    @Transactional
    void 호출_주변에_엔젤이_있는_경우() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber("user1").get();
        User cprAngel = userRepository.findByPhoneNumber("angel1").get();

        makeCallInAngelArea(caller, cprAngel, latitude ,longitude);

        //when
        var callListForAngel = cprCallService.getCallNearUser(cprAngel);

        //then
        assertThat(callListForAngel.getCprCallResponseDtoList().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void 호출_주변에_일반인이_있는_경우() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber("user1").get();
        User notAngel = userRepository.findByPhoneNumber("user2").get();

        makeCallInAngelArea(caller, notAngel, latitude ,longitude);

        //when
        var callListForNotAngel = cprCallService.getCallNearUser(notAngel);

        //then
        assertThat(callListForNotAngel.getCprCallResponseDtoList().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void 호출_종료() {
        //given
        createUsers();

        User caller = userRepository.findByPhoneNumber("user1").get();
        User cprAngel = userRepository.findByPhoneNumber("angel1").get();

        var callId = makeCallInAngelArea(caller, cprAngel, latitude ,longitude);
        cprCallService.endCall(callId);

        //when
        var callListForAngel = cprCallService.getCallNearUser(cprAngel);

        //then
        assertThat(callListForAngel.getCprCallResponseDtoList().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void 호출_종료_출동한_엔젤이_있는_경우() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber("user1").get();
        User dispatcher = userRepository.findByPhoneNumber("angel1").get();

        Long callId = makeCallInAngelArea(caller, dispatcher, latitude, longitude);
        var dispatchInfo = dispatchService.dispatch(dispatcher, new DispatchRequestDto(callId));

        //when
        cprCallService.endCall(callId);

        //then
        CprCall cprCall = cprCallRepository.findById(callId).get();
        assertThat(cprCall.getStatus()).isEqualTo(CprCallStatus.END_SITUATION);

        Dispatch dispatch = dispatchRepository.findById(dispatchInfo.getDispatchId()).get();
        assertThat(dispatch.getStatus()).isEqualTo(DispatchStatus.END_SITUATION);
    }

    //@Test
//    @Transactional
//    void makeCall() {
//        //given
//        User user = userRepository.findByPhoneNumber("user1").get();
//
//        //when
//        Long callId1 = cprCallService.makeCall(new CprCallRequestDto("서울 종로구 종로 104", 37.56559872345163, 126.9779734762639), user).getCallId();
//        Long callId2 = cprCallService.makeCall(new CprCallRequestDto("서울 중구 세종대로 지하 2", 37.56559872345163, 126.9779734762639), user).getCallId();
//        Long callId3 = cprCallService.makeCall(new CprCallRequestDto("세종특별자치시 한누리대로 2130 (우)30151", 37.56559872345163, 126.9779734762639), user).getCallId();
//        Long callId4 = cprCallService.makeCall(new CprCallRequestDto("경남 창원시 진해구 평안동 10", 37.56559872345163, 126.9779734762639), user).getCallId();
//
//        //then
//        CprCall cprCall1 = cprCallRepository.findById(callId1).get();
//        CprCall cprCall2 = cprCallRepository.findById(callId2).get();
//        CprCall cprCall3 = cprCallRepository.findById(callId3).get();
//        CprCall cprCall4 = cprCallRepository.findById(callId4).get();
//
//        assertThat(cprCall1.getAddress().getId()).isEqualTo(1L);
//        assertThat(cprCall2.getAddress().getId()).isEqualTo(2L);
//        assertThat(cprCall3.getAddress().getId()).isEqualTo(75L);
//        assertThat(cprCall4.getAddress().getId()).isEqualTo(231L);
//
//        assertThat(cprCall1.getStatus()).isEqualTo(CprCallStatus.IN_PROGRESS);
//
//    }

    @Test
    @Transactional
    void 실시간_출동_안내_출동한_엔젤이_없는_경우() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber("user1").get();
        Long callId = cprCallService.makeCall(new CprCallRequestDto("서울시 동작구", latitude, longitude), caller)
                .getCallId();

        //when
        var callGuide = cprCallService.getNumberOfAngelsDispatched(callId);

        //then
        assertThat(callGuide.getNumberOfAngels()).isEqualTo(0);
    }

    @Test
    @Transactional
    void 실시간_출동_안내_출동한_엔젤이_있는_경우() {
        //given
        createUsers();
        User caller = userRepository.findByPhoneNumber("user1").get();
        User dispatcher = userRepository.findByPhoneNumber("angel1").get();

        Long callId = makeCallInAngelArea(caller, dispatcher, latitude, longitude);
        dispatchService.dispatch(dispatcher, new DispatchRequestDto(callId));

        //when
        var callGuide = cprCallService.getNumberOfAngelsDispatched(callId);

        //then
        assertThat(callGuide.getNumberOfAngels()).isEqualTo(1);
    }

    public void createUsers() {
        var address = addressService.readAll().get(0).getGugunList().get(0);
        userService.signup(new SignUpRequestDto("nickname1", "user1", address.getId(), "deviceToken"));
        userService.signup(new SignUpRequestDto("nickname2", "user2", address.getId(), "deviceToken"));
        userService.signup(new SignUpRequestDto("nickname3", "angel1", address.getId(), "deviceToken"));

        User angel = userRepository.findByPhoneNumber("angel1").get();
        angel.acquireCertification(LocalDateTime.now());
        userRepository.save(angel);
    }

    private Long makeCallInAngelArea(User caller, User angel, double latitude, double longitude) {
        Address address = angel.getAddress();

        System.out.println(address.getSido()+" "+address.getSigugun()+" 대성리");

        return cprCallService.makeCall(
                new CprCallRequestDto(address.getSido()+" "+address.getSigugun()+" 대성리", latitude, longitude), caller)
                .getCallId();
    }
}