package com.hua.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationDTO {

    private List<String> admins;

    private List<String> usernames;
}
