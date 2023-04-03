package com.hua.api.repository;

import com.hua.api.entity.HuaEvent;
import com.hua.api.enums.EventTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HuaEventRepository extends JpaRepository<HuaEvent, Long> {

    List<HuaEvent> findAllByAdminInformedAndEventTypeOrderByCreatedDateDesc(boolean isInformed, EventTypeEnum eventType);

    List<HuaEvent> findAllByEventTypeAndCreatedDateBetweenOrderByCreatedDateDesc(EventTypeEnum eventType, LocalDateTime from, LocalDateTime to);

    List<HuaEvent> findAllByCreatedDateBetweenOrderByCreatedDateDesc(LocalDateTime from, LocalDateTime to);
}
