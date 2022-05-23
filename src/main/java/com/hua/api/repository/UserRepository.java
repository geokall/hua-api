package com.hua.api.repository;

import com.hua.api.entity.HuaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<HuaUser, Long> {

    HuaUser findByUsername(String username);
}
