package com.hua.api.service;

import com.hua.api.dto.StudentDTO;
import com.hua.api.dto.StudentDirectionDTO;
import com.hua.api.enums.GenderEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Test
    public void findStudent() {
        var request = new StudentDTO();
        request.setAddress("address");
        request.setCity("city");
        request.setBirthDate(new Date().toString());
        request.setDepartment("department");
        request.setDirection(new StudentDirectionDTO());
        request.setEmail("email");
        request.setFatherName("father name");
        request.setMotherName("mother name");
        request.setGender(GenderEnum.MALE);
        request.setIsVerified(false);
        request.setMobileNumber("2100000012");
        request.setName("name");
        request.setSurname("surname");
        request.setVatNumber("vat number");
        request.setUsername("username");

        studentService.createStudent(request);

        StudentDTO student = studentService.findStudent(200L);
        assertNotNull(student);
        assertEquals(1L, student.getId().longValue());
    }
}