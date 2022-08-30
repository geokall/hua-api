package com.hua.api.repository;

import com.hua.api.entity.HuaEvent;
import com.hua.api.enums.EventTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HuaEventRepository extends JpaRepository<HuaEvent, Long> {

    List<HuaEvent> findAllByAdminInformedAndEventType(boolean isInformed, EventTypeEnum eventType);
}
