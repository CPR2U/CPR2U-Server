package com.mentionall.cpr2u.user.domain;

import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.util.RandomGenerator;
import com.mentionall.cpr2u.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped{

    @Id
    @GeneratedValue(generator = RandomGenerator.generatorName)
    @GenericGenerator(name = RandomGenerator.generatorName, strategy = "com.mentionall.cpr2u.util.RandomGenerator")
    @Column
    private String id;

    @Column
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_issue")
    private LocalDateTime dateOfIssue;

    @Column
    @Enumerated(EnumType.STRING)
    private AngelStatusEnum status;

    @Column(name = "device_token")
    private String deviceToken;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = new ArrayList<>();

    public User(UserSignUpDto userSignUpDto) {
        this.nickname = userSignUpDto.getNickName();
        this.phoneNumber = userSignUpDto.getPhoneNumber();
        this.dateOfIssue = null;
        this.deviceToken = userSignUpDto.getDeviceToken();
        this.status = AngelStatusEnum.UNACQUIRED;
        this.roles.add(UserRole.USER);
    }
}
