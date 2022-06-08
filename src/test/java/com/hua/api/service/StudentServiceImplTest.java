package com.hua.api.service;

import com.hua.api.dto.StudentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @Test
    public void findStudent() {
        StudentDTO dto = new StudentDTO();
        dto.setId(1L);
        StudentDTO student = studentService.findStudent(1L);
        assertEquals(student.getId(), dto.getId());
    }
}