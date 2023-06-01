package com.mentionall.cpr2u.user.domain.token;

import com.mentionall.cpr2u.user.domain.User;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken {

    @Id
    private String refreshToken;
    private String userId;

    public RefreshToken(User user, String refreshToken) {
        this.userId = user.getId();
        this.refreshToken = refreshToken;
    }
}
