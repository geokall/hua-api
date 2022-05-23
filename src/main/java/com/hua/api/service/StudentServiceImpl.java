package com.hua.api.service;

import com.hua.api.dto.*;
import com.hua.api.entity.HuaContactInfo;
import com.hua.api.entity.HuaStudentDetails;
import com.hua.api.entity.HuaUser;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.ContactInfoRepository;
import com.hua.api.repository.UserRepository;
import com.hua.api.utilities.HuaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              ContactInfoRepository contactInfoRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long createStudent(StudentDTO studentDTO) {

        StudentContactInfoDTO studentContactInfo = studentDTO.getStudentContactInfo();

        handleMobileAndAfmUniqueness(studentContactInfo);

        HuaUser user = new HuaUser();

        setBasicInfo(studentDTO, user);

        var studentDetailsDTO = studentDTO.getStudentDetails();

        var listOfStudentDetails = getStudentDetails(studentDetailsDTO, user);

        user.setStudentDetails(listOfStudentDetails);

        var studentContactInfoDTO = studentDTO.getStudentContactInfo();

        var listOfContactInfo = getContactInfos(studentContactInfoDTO, user);

        user.setContactInfos(listOfContactInfo);

        HuaUser savedUser = userRepository.save(user);

        userRepository.findById(savedUser.getId())
                .ifPresent(huaUser -> {
                    HuaUser updatedUser = generateUsernameAndEmail(huaUser);
                    userRepository.save(updatedUser);
                });

        return savedUser.getId();
    }


    private void handleMobileAndAfmUniqueness(StudentContactInfoDTO studentContactInfo) {
        if (!ObjectUtils.isEmpty(studentContactInfo.getMobileNumber())) {
            contactInfoRepository.findByMobileNumber(studentContactInfo.getMobileNumber())
                    .ifPresent(student -> {
                        throw new HuaNotFound("Υπάρχει φοιτητής με το ίδιο κινητό");
                    });
        }

        if (!ObjectUtils.isEmpty(studentContactInfo.getVatNumber())) {
            contactInfoRepository.findByVatNumber(studentContactInfo.getVatNumber())
                    .ifPresent(student -> {
                        throw new HuaNotFound("Υπάρχει φοιτητής με τον ίδιο Α.Φ.Μ");
                    });
        }
    }

    private HuaUser generateUsernameAndEmail(HuaUser user) {
        String generatedUsername = HuaUtil.generateUsername(user.getId());
        user.setUsername(generatedUsername);

        String generatedEmail = HuaUtil.generateEmail(generatedUsername);
        user.setEmail(generatedEmail);

        return user;
    }

    private List<HuaContactInfo> getContactInfos(StudentContactInfoDTO studentContactInfoDTO, HuaUser user) {
        List<HuaContactInfo> listOfContactInfo = new ArrayList<>();

        HuaContactInfo contactInfo = new HuaContactInfo();
        contactInfo.setAddress(studentContactInfoDTO.getAddress());
        contactInfo.setCity(studentContactInfoDTO.getCity());
        contactInfo.setPostalCode(studentContactInfoDTO.getPostalCode());
        contactInfo.setMobileNumber(studentContactInfoDTO.getMobileNumber());
        contactInfo.setVatNumber(studentContactInfoDTO.getVatNumber());
        contactInfo.setHuaUser(user);

        listOfContactInfo.add(contactInfo);

        return listOfContactInfo;
    }

    private List<HuaStudentDetails> getStudentDetails(StudentDetailsDTO studentDetailsDTO, HuaUser user) {
        List<HuaStudentDetails> listOfStudentDetails = new ArrayList<>();
        HuaStudentDetails studentDetails = new HuaStudentDetails();
        studentDetails.setDepartment(studentDetailsDTO.getDepartment());
        studentDetails.setHuaUser(user);

        var directionDTO = studentDetailsDTO.getDirection();

        if (directionDTO != null) {
            studentDetails.setDirection(directionDTO.getName());
        }

        listOfStudentDetails.add(studentDetails);

        return listOfStudentDetails;
    }

    private void setBasicInfo(StudentDTO studentDTO, HuaUser user) {
        user.setDateCreated(LocalDateTime.now());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("temp");
        user.setUsername("temp");

        user.setSurname(studentDTO.getSurname());
        user.setName(studentDTO.getName());
        user.setFatherName(studentDTO.getFatherName());
        user.setMotherName(studentDTO.getMotherName());

        String birthDate = studentDTO.getBirthDate();

        if (!ObjectUtils.isEmpty(birthDate)) {
            Date birthDateFormatted = HuaUtil.formatStringToDate(birthDate);
            user.setBirthDate(birthDateFormatted);
        }

        user.setGender(studentDTO.getGender());
    }
}
