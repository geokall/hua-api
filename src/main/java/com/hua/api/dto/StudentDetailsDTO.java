package com.hua.api.dto;

import lombok.Data;

@Data
public class StudentDetailsDTO {

    private Long id;

    private String department;

    private StudentDirectionDTO direction;
}
