package com.hua.api.repository;

import com.hua.api.entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HuaUser, Long> {

    HuaUser findByUsername(String username);

    Optional<HuaUser> findByVatNumber(String vatNumber);

    Optional<HuaUser> findByMobileNumber(String mobileNumber);

    List<HuaUser> findByRoles_Id(Long id);
}
