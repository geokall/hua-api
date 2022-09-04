package com.hua.api.dto;

import com.hua.api.entity.HuaEvent;
import com.hua.api.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private Long id;

    private EventTypeEnum eventType;

    private boolean adminInformed;

    private String details;

    private LocalDateTime createdDate;

    private StudentDTO student;

    public EventDTO(HuaEvent huaEvent) {
        this.id = huaEvent.getId();
        this.adminInformed = huaEvent.isAdminInformed();
        this.eventType = huaEvent.getEventType();
        this.createdDate = huaEvent.getCreatedDate();
        this.details = huaEvent.getDetails();
        this.student = new StudentDTO(huaEvent.getHuaUser());
    }
}
