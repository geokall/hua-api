package com.hua.api.dto;

import com.hua.api.enums.GenderEnum;
import lombok.Data;

@Data
public class StudentGenderDTO {

    private Long id;

    private GenderEnum name;
}
