package com.mentionall.cpr2u.user.repository;

import com.mentionall.cpr2u.user.domain.Address;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mentionall.cpr2u.user.domain.QAddress.address;


public class AddressRepositoryImpl implements AddressDslRepository {

    private final JPAQueryFactory queryFactory;

    public AddressRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Address findByFullAddress(String[] addressList) {
        List<Address> resultAddressList = queryFactory.selectFrom(address)
                .where(address.sido.contains(addressList[0]))
                .fetch();

        for(int i = 1 ; resultAddressList.size() != 1  && i <= 2; i ++) {
            resultAddressList = findAddressByAddressString(address.sigugun, addressList, i).fetch();
        }

        return resultAddressList.get(0);

    }

    private JPAQuery<Address> findAddressByAddressString(StringPath addressDetail, String[] addressList, int x) {
        return queryFactory.selectFrom(address)
                .where(addressDetail.contains(addressList[x])
                        .and(address.sido.contains(addressList[0])));
    }
}
