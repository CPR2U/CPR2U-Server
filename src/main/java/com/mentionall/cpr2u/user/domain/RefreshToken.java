package com.mentionall.cpr2u.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken(String refreshToken, User user) {
        this.user = user;
        this.token = refreshToken;
    }

    public RefreshToken(User user) {
        this.user = user;
    }

    public void setToken(String refreshToken) {
        this.token = refreshToken;
    }
}
