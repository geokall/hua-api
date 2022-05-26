package com.hua.api.repository;

import com.hua.api.entity.HuaRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<HuaRole, Long> {

    List<HuaRole> findByRoles_Id(Long id);
}
