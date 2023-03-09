package com.mentionall.cpr2u.auth.service;

import com.mentionall.cpr2u.config.security.JwtTokenProvider;
import com.mentionall.cpr2u.user.domain.Address;
import com.mentionall.cpr2u.user.domain.User;
import com.mentionall.cpr2u.user.dto.AddressRequestDto;
import com.mentionall.cpr2u.user.dto.AddressResponseDto;
import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.user.repository.AddressRepository;
import com.mentionall.cpr2u.user.repository.UserRepository;
import com.mentionall.cpr2u.user.service.AddressService;
import com.mentionall.cpr2u.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressServiceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;



    @Test
    @Transactional
    public void setAddress() {
        //given
        String userId = getUserId("현애", "010-0000-0000", "device-token");
        Address address = new Address("서울특별시", "용산구");
        addressRepository.save(address);

        //when
        addressService.setAddress(userId, new AddressRequestDto(address.getId()));

        //then
        User user = userRepository.findById(userId).orElse(null);
        assertThat(user.getAddress().getId()).isEqualTo(address.getId());
        assertThat(user.getAddress().getSido()).isEqualTo("서울특별시");
        assertThat(user.getAddress().getSigugun()).isEqualTo("용산구");
    }

    @Test
    @Transactional
    public void readAll() {
        //given
        addressRepository.save(new Address("서울특별시", "용산구"));
        addressRepository.save(new Address("서울특별시", "중구"));
        addressRepository.save(new Address("서울특별시", "종로구"));
        addressRepository.save(new Address("서울특별시", "마포구"));


        //when
        List<AddressResponseDto> response = addressService.readAll();

        //then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getSido()).isEqualTo("서울특별시");
        assertThat(response.get(0).getGugunList().size()).isEqualTo(4);
    }

    private String getUserId(String nickname, String phoneNumber, String deviceToken) {
        String accessToken = userService.signup(new UserSignUpDto(nickname, phoneNumber, deviceToken)).getAccessToken();
        return jwtTokenProvider.getUserId(accessToken);
    }

}
