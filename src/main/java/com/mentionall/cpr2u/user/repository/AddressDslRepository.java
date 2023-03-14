package com.mentionall.cpr2u.user.repository;

import com.mentionall.cpr2u.user.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AddressDslRepository {
    Address findByFullAddress(String[] addressList);
}
