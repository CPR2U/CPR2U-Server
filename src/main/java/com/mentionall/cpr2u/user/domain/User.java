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

    @Column
    private String phoneNumber;

    @Column
    private LocalDateTime dateOfIssue;

    @Column
    @Enumerated(EnumType.STRING)
    private AngelStatusEnum status;

    @Column
    private String deviceToken;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = new ArrayList<>();

    public User(UserSignUpDto userSignUpDto) {
        this.nickname = userSignUpDto.getNickname();
        this.phoneNumber = userSignUpDto.getPhoneNumber();
        this.dateOfIssue = null;
        this.deviceToken = userSignUpDto.getDeviceToken();
        this.status = AngelStatusEnum.UNACQUIRED;
        this.roles.add(UserRole.USER);
    }
}
