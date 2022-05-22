package com.hua.api.service;

import com.hua.api.dto.StudentContactInfoDTO;
import com.hua.api.dto.StudentDTO;
import com.hua.api.dto.StudentDetailsDTO;
import com.hua.api.entity.HuaContactInfo;
import com.hua.api.entity.HuaStudentDetails;
import com.hua.api.entity.HuaUser;
import com.hua.api.repository.ContactInfoRepository;
import com.hua.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final ContactInfoRepository contactInfoRepository;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              ContactInfoRepository contactInfoRepository) {
        this.userRepository = userRepository;
        this.contactInfoRepository = contactInfoRepository;
    }

    @Override
    public Long createStudent(StudentDTO studentDTO) {
        HuaUser user = new HuaUser();

        setBasicInfo(studentDTO, user);

        var studentDetailsDTO = studentDTO.getStudentDetails();

        List<HuaStudentDetails> listOfStudentDetails = getStudentDetails(studentDetailsDTO);

        user.setStudentDetails(listOfStudentDetails);

        var studentContactInfoDTO = studentDTO.getStudentContactInfo();

        List<HuaContactInfo> listOfContactInfo = getContactInfos(studentContactInfoDTO);

        user.setContactInfos(listOfContactInfo);

        HuaUser savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    private List<HuaContactInfo> getContactInfos(StudentContactInfoDTO studentContactInfoDTO) {
        List<HuaContactInfo> listOfContactInfo = new ArrayList<>();

        HuaContactInfo contactInfo = new HuaContactInfo();
        contactInfo.setAddress(studentContactInfoDTO.getAddress());
        contactInfo.setCity(studentContactInfoDTO.getCity());
        contactInfo.setPostalCode(studentContactInfoDTO.getPostalCode());
        contactInfo.setMobileNumber(studentContactInfoDTO.getMobileNumber());
        contactInfo.setVatNumber(studentContactInfoDTO.getVatNumber());

        listOfContactInfo.add(contactInfo);

        return listOfContactInfo;
    }


    private List<HuaStudentDetails> getStudentDetails(StudentDetailsDTO studentDetailsDTO) {
        List<HuaStudentDetails> listOfStudentDetails = new ArrayList<>();
        HuaStudentDetails studentDetails = new HuaStudentDetails();
        studentDetails.setDepartment(studentDetailsDTO.getDepartment());
        studentDetails.setDirection(studentDetailsDTO.getDirection());

        listOfStudentDetails.add(studentDetails);

        return listOfStudentDetails;
    }

    private void setBasicInfo(StudentDTO studentDTO, HuaUser user) {
        user.setSurname(studentDTO.getSurname());
        user.setName(studentDTO.getName());
        user.setFatherName(studentDTO.getFatherName());
        user.setMotherName(studentDTO.getMotherName());
        user.setBirthDate(studentDTO.getBirthDate());
        user.setGender(studentDTO.getGender());
    }
}
