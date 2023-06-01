package com.mentionall.cpr2u.call.repository;

import com.mentionall.cpr2u.call.domain.Dispatch;
import com.mentionall.cpr2u.call.domain.DispatchStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mentionall.cpr2u.call.domain.QDispatch.dispatch;

public class DispatchRepositoryImpl implements DispatchDslRepository {

    private final JPAQueryFactory queryFactory;

    public DispatchRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Dispatch> findAllNotArrivedAngelByCprCallId(Long cprCallId) {
        return queryFactory.selectFrom(dispatch)
                .where(dispatch.status.eq(DispatchStatus.IN_PROGRESS)
                        .and(dispatch.cprCall.id.eq(cprCallId)))
                .fetch();
    }

    @Override
    public boolean existsByCprCallIdAndUserId(Long cprCallId, String userId) {
        Dispatch foundDispatch = queryFactory.selectFrom(dispatch)
                .where(dispatch.cprCall.id.eq(cprCallId)
                    .and(dispatch.dispatcher.id.eq(userId)))
                .fetchFirst();
        return foundDispatch == null ? false : true;
    }
}
