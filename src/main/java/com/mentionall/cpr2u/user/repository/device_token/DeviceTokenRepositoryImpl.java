package com.mentionall.cpr2u.user.repository.device_token;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mentionall.cpr2u.user.domain.QDeviceToken.deviceToken;

public class DeviceTokenRepositoryImpl implements DeviceTokenDslRepository {

    private final JPAQueryFactory queryFactory;

    public DeviceTokenRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findAllDeviceTokenByUserAddress(Long addressId, String userId, Integer offset, Integer limit) {
        return queryFactory.select(deviceToken.token)
                .from(deviceToken)
                .where(deviceToken.user.address.id.eq(addressId)
                        .and(deviceToken.user.id.ne(userId)))
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
