package com.hua.api.repository;

import com.hua.api.entity.HuaContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactInfoRepository extends JpaRepository<HuaContactInfo, Long> {

    Optional<HuaContactInfo> findByMobileNumber(String mobileNumber);

    Optional<HuaContactInfo> findByVatNumber(String vatNumber);
}
