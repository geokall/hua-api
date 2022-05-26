package com.hua.api.service;

import com.hua.api.dto.*;
import com.hua.api.entity.HuaUser;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.RoleRepository;
import com.hua.api.repository.UserRepository;
import com.hua.api.utilities.HuaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String TEMP = "temp";
    private static final String READER_ROLE = "READER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long createStudent(StudentDTO studentDTO) {

        handleMobileAndAfmUniqueness(studentDTO.getMobileNumber(), studentDTO.getVatNumber());

        HuaUser user = new HuaUser();

        setBasicInfo(studentDTO, user);

        setStudentDetails(studentDTO, user);

        setContactInfo(studentDTO, user);

        roleRepository.findByName(READER_ROLE)
                .ifPresent(user::addRole);

        HuaUser savedUser = userRepository.save(user);

        userRepository.findById(savedUser.getId())
                .ifPresent(huaUser -> {
                    HuaUser updatedUser = generateUsernameAndEmail(huaUser);
                    userRepository.save(updatedUser);
                });

        return savedUser.getId();
    }


    private void handleMobileAndAfmUniqueness(String mobileNumber, String vatNumber) {
        if (!ObjectUtils.isEmpty(mobileNumber)) {
            userRepository.findByMobileNumber(mobileNumber)
                    .ifPresent(student -> {
                        throw new HuaNotFound("Υπάρχει φοιτητής με το ίδιο κινητό");
                    });
        }

        if (!ObjectUtils.isEmpty(vatNumber)) {
            userRepository.findByVatNumber(vatNumber)
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

    private void setBasicInfo(StudentDTO studentDTO, HuaUser user) {
        user.setDateCreated(LocalDateTime.now());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(TEMP));
        user.setEmail(TEMP);
        user.setUsername(TEMP);

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

    private void setContactInfo(StudentDTO studentDTO, HuaUser user) {
        user.setAddress(studentDTO.getAddress());
        user.setCity(studentDTO.getCity());
        user.setPostalCode(studentDTO.getPostalCode());
        user.setMobileNumber(studentDTO.getMobileNumber());
        user.setVatNumber(studentDTO.getVatNumber());
    }

    private void setStudentDetails(StudentDTO studentDTO, HuaUser user) {
        user.setDepartment(studentDTO.getDepartment());

        var directionDTO = studentDTO.getDirection();

        if (directionDTO != null) {
            user.setDirection(directionDTO.getName());
        }
    }
}
