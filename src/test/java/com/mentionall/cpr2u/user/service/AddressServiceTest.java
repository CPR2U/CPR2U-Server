package com.mentionall.cpr2u.user.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.address.AddressRequestDto;
import com.mentionall.cpr2u.user.dto.address.AddressResponseDto;
import com.mentionall.cpr2u.user.dto.user.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressServiceTest {
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void beforeEach() {
        addressService.loadAddressList();
    }

    @Test
    @DisplayName("사용자의 주소지 설정")
    @Transactional
    public void setAddress() {
        //given
        var tokens = userService.signup(new UserSignUpDto("현애", "010-0000-0000", "device-token"));
        String userId = jwtTokenProvider.getUserId(tokens.getAccessToken());
        User user = userRepository.findById(userId).get();

        List<AddressResponseDto> addressList = addressService.readAll();
        var address = addressList.get(0);
        var addressDetail = address.getGugunList().get(0);

        //when
        addressService.setAddress(user, new AddressRequestDto(addressDetail.getId()));

        //then
        User findUser = userRepository.findById(userId).get();
        assertThat(findUser.getAddress().getSido()).isEqualTo(address.getSido());
        assertThat(findUser.getAddress().getId()).isEqualTo(addressDetail.getId());
        assertThat(findUser.getAddress().getSigugun()).isEqualTo(addressDetail.getGugun());
    }

    @Test
    @DisplayName("전체 주소지 리스트 조회")
    @Transactional
    public void readAll() {
        //given

        //when
        List<AddressResponseDto> response = addressService.readAll();

        //then
        assertThat(response.size()).isEqualTo(16);                           // count of sido
        assertThat(response.get(0).getSido()).isEqualTo("강원도");
        assertThat(response.get(0).getGugunList().size()).isEqualTo(18);     // count of sigugun
    }
}
