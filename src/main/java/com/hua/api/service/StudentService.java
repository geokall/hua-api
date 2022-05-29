package com.hua.api.service;

import com.hua.api.dto.StudentDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    Long createStudent(StudentDTO studentDTO);

    StudentDTO findStudent(Long id);

    void updateStudent(Long id, StudentDTO studentDTO);

    List<StudentDTO> findAllStudents(Pageable pageable);
}
