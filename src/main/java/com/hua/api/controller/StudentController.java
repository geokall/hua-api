package com.hua.api.controller;

import com.hua.api.dto.StudentDTO;
import com.hua.api.exception.HuaExceptionHandler;
import com.hua.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController extends HuaExceptionHandler {

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

    @GetMapping("/all")
    public ResponseEntity<Page<StudentDTO>> findAllStudents(Pageable pageable) {

        Page<StudentDTO> response = studentService.findAllStudents(pageable);

        return ResponseEntity.ok(response);
    }
}
