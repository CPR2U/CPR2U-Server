package com.mentionall.cpr2u.user.domain;

import com.mentionall.cpr2u.user.dto.UserSignUpDto;
import com.mentionall.cpr2u.util.RandomGenerator;
import com.mentionall.cpr2u.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped implements UserDetails {

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

    public User(UserSignUpDto userSignUpDto) {
        this.nickname = userSignUpDto.getNickName();
        this.phoneNumber = userSignUpDto.getPhoneNumber();
        this.dateOfIssue = null;
        this.status = AngelStatusEnum.UNACQUIRED;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
