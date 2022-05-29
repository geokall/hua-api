package com.hua.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponseDTO {

    private Long id;

    private String token;

    private String username;

    private String surname;

    private String name;

    private String email;

    private List<String> roles;
}
