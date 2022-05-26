package com.hua.api.service;

import com.hua.api.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Long createStudent(StudentDTO studentDTO);

    Page<StudentDTO> findAllStudents(Pageable pageable);
}
