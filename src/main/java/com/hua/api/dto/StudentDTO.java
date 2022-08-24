package com.hua.api.dto;

import com.hua.api.enums.GenderEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentDTO {

    private Long id;

    private String username;

    private String email;

    private LocalDateTime dateCreated;

    private LocalDateTime dateChanged;

    private Boolean isVerified;

    private String surname;

    private String name;

    private String fatherName;

    private String motherName;

    private String birthDate;

    private GenderEnum gender;

    //Στοιχεία φοίτησης
    private String department;

    private StudentDirectionDTO direction;

    //Στοιχεία επικοινωνίας
    private String address;

    private String city;

    private String postalCode;

    private String mobileNumber;

    private String vatNumber;

    private FileDTO file;
}
