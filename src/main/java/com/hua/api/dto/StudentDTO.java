package com.hua.api.dto;

import com.hua.api.entity.HuaUser;
import com.hua.api.enums.GenderEnum;
import com.hua.api.utilities.HuaUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public StudentDTO(HuaUser user) {
        this.id = user.getId();
        this.dateChanged = user.getDateChanged();
        this.dateCreated = user.getDateCreated();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.department = user.getDepartment();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.isVerified = user.getVerified() != null ? user.getVerified() : false;

        StudentDirectionDTO direction = new StudentDirectionDTO();
        direction.setName(user.getDirection());
        this.direction = direction;

        if (user.getBirthDate() != null) {
            String birthDateFormatted = HuaUtil.formatDateToString(user.getBirthDate());
            this.birthDate = birthDateFormatted;
        }

        this.gender = user.getGender();
        this.fatherName  = user.getFatherName();
        this.mobileNumber = user.getMobileNumber();
        this.motherName = user.getMotherName();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.postalCode = user.getPostalCode();
        this.vatNumber = user.getVatNumber();
    }
}
