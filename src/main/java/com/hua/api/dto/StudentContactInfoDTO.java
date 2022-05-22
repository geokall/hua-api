package com.hua.api.dto;

import lombok.Data;

@Data
public class StudentContactInfoDTO {

    private Long id;

    private String address;

    private String city;

    private String postalCode;

    private String mobileNumber;

    private String vatNumber;
}
