package com.mentionall.cpr2u.user.domain;

import com.mentionall.cpr2u.education.domain.EducationProgress;
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
    @Column(length = 20)
    private String id;

    @Column(length = 40)
    private String nickname;

    @Column(length = 20)
    private String phoneNumber;

    @Column
    private LocalDateTime dateOfIssue;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private AngelStatusEnum status;

    @OneToOne(mappedBy = "user")
    private EducationProgress educationProgress;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private DeviceToken deviceToken;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<UserRole> roles = new ArrayList<>();

    public User(UserSignUpDto userSignUpDto) {
        this.nickname = userSignUpDto.getNickname();
        this.phoneNumber = userSignUpDto.getPhoneNumber();
        this.dateOfIssue = null;
        this.status = AngelStatusEnum.UNACQUIRED;
        this.roles.add(UserRole.USER);
    }

    public void setDeviceToken(DeviceToken deviceToken) {
        this.deviceToken = deviceToken;
    }
}
