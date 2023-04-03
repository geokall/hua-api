package com.hua.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hua.api.dto.EventDTO;
import com.hua.api.entity.HuaEvent;
import com.hua.api.entity.HuaUser;
import com.hua.api.enums.EventTypeEnum;
import com.hua.api.repository.HuaEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final HuaEventRepository repository;
    private final ObjectMapper objectMapper;

    public EventServiceImpl(HuaEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public EventDTO create(EventTypeEnum eventType, HuaUser user) throws JsonProcessingException {
        return create(eventType, user, Collections.EMPTY_MAP);
    }

    @Override
    public EventDTO create(EventTypeEnum eventType, HuaUser user, Map detailsMap) throws JsonProcessingException {
        var event = new HuaEvent();
        event.setEventType(eventType);
        event.setCreatedDate(LocalDateTime.now());
        event.setAdminInformed(false);
        event.setHuaUser(user);

        String json = objectMapper.writeValueAsString(detailsMap);
        event.setDetails(json);

        event = repository.save(event);
        return new EventDTO(event);
    }

    @Override
    public List<EventDTO> get(boolean isAdminInformed, EventTypeEnum eventType) {
        return repository.findAllByAdminInformedAndEventTypeOrderByCreatedDateDesc(isAdminInformed, eventType)
                .stream()
                .map(EventDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO update(EventDTO dto) {
        return repository.findById(dto.getId())
                .map(e -> {
                    e.setDetails(dto.getDetails());
                    e.setAdminInformed(dto.isAdminInformed());
                    e = repository.save(e);
                    return new EventDTO(e);
                })
                .orElse(null);
    }

    @Override
    public List<EventDTO> findAll(LocalDate from, LocalDate to) {
        return repository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(from.atStartOfDay(), to.atStartOfDay())
                .stream()
                .map(EventDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> findAll(LocalDate from, LocalDate to, EventTypeEnum eventType) {
        return repository.findAllByEventTypeAndCreatedDateBetweenOrderByCreatedDateDesc(eventType, from.atStartOfDay(), to.atStartOfDay())
                .stream()
                .map(EventDTO::new)
                .collect(Collectors.toList());
    }
}
