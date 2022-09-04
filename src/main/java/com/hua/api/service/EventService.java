package com.hua.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hua.api.dto.EventDTO;
import com.hua.api.entity.HuaUser;
import com.hua.api.enums.EventTypeEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EventService {

    EventDTO create(EventTypeEnum eventType, HuaUser user)throws JsonProcessingException;
    EventDTO create(EventTypeEnum eventType, HuaUser user, Map detailsMap) throws JsonProcessingException;
    List<EventDTO> get(boolean isAdminInformed, EventTypeEnum eventType);
    EventDTO update(EventDTO eventDto);

    List<EventDTO> findAll(LocalDate from, LocalDate to);
    List<EventDTO> findAll(LocalDate from, LocalDate to, EventTypeEnum eventType);
}
