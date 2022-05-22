package com.hua.api.dto;

import com.hua.api.enums.GenderEnum;
import lombok.Data;

import java.util.Date;

@Data
public class StudentDTO {

    private Long id;

    private String surname;

    private String name;

    private String fatherName;

    private String motherName;

    private Date birthDate;

    private GenderEnum gender;

    private StudentDetailsDTO studentDetails; //Στοιχεία φοίτησης

    private StudentContactInfoDTO studentContactInfo; //Στοιχεία επικοινωνίας
}
