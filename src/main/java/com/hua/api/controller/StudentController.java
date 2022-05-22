package com.hua.api.controller;

import com.hua.api.dto.StudentDTO;
import com.hua.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createStudent(@RequestBody StudentDTO studentDTO) {

        Long studentId = studentService.createStudent(studentDTO);

        return ResponseEntity.ok(studentId);
    }
}
