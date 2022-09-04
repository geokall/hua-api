package com.hua.api.dto;

import com.hua.api.enums.EventTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO {

    private EventTypeEnum eventType;

    private boolean adminInformed;

    private String details;

    private LocalDateTime createdDate;

    private StudentDTO student;
}
