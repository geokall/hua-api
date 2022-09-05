package com.hua.api.dto;

import com.hua.api.enums.EventTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RabbitMqDTO {

    private Long eventId;

    private EventTypeEnum eventType;

    private String email;

    private LocalDateTime createdDate;

    private Long userId;
}
