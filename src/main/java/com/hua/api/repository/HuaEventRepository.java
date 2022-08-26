package com.hua.api.repository;

import com.hua.api.entity.HuaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HuaEventRepository extends JpaRepository<HuaEvent, Long> {
}
