package com.hua.api.controller;

import com.hua.api.dto.FileDTO;
import com.hua.api.dto.PasswordDTO;
import com.hua.api.dto.StudentDTO;
import com.hua.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class StudentController extends BaseController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student/create")
    public ResponseEntity<Long> createStudent(@RequestBody StudentDTO studentDTO) {

        Long studentId = studentService.createStudent(studentDTO);

        return ResponseEntity.ok(studentId);
    }

    @GetMapping("/student/find/{id}")
    public ResponseEntity<StudentDTO> findStudent(@PathVariable Long id) {

        StudentDTO studentId = studentService.findStudent(id);

        return ResponseEntity.ok(studentId);
    }

    @PutMapping("/student/update/{id}")
    public ResponseEntity<Long> updateStudent(@PathVariable Long id,
                                              @RequestBody StudentDTO dto) throws IOException {

        studentService.updateStudent(id, dto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/student/update-password/{id}")
    public ResponseEntity<Long> updatePassword(@PathVariable Long id,
                                               @RequestBody PasswordDTO dto) {

        studentService.updatePassword(id, dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/all")
    public ResponseEntity<List<StudentDTO>> findAllStudents(Pageable pageable) {

        List<StudentDTO> response = studentService.findAllStudents(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/minio-file/{username}")
    public ResponseEntity<FileDTO> fetchMinioFile(@PathVariable String username) {

        FileDTO response = studentService.fetchMinioFile(username);

        return ResponseEntity.ok(response);
    }
}
