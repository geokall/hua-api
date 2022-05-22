package com.hua.api.dto;

import lombok.Data;

@Data
public class StudentDTO {

    private Long id;

    private String surname;

    private String name;

    private String fatherName;

    private String motherName;

    private String birthDate;

    private StudentGenderDTO gender;

    private StudentDetailsDTO studentDetails; //Στοιχεία φοίτησης

    private StudentContactInfoDTO studentContactInfo; //Στοιχεία επικοινωνίας
}
