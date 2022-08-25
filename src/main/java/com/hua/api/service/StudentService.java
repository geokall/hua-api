package com.hua.api.service;

import com.hua.api.dto.FileDTO;
import com.hua.api.dto.PasswordDTO;
import com.hua.api.dto.StudentDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    Long createStudent(StudentDTO studentDTO);

    StudentDTO findStudent(Long id);

    void updateStudent(Long id, StudentDTO studentDTO) throws IOException;

    void updatePassword(Long id, PasswordDTO passwordDTO);

    List<StudentDTO> findAllStudents(Pageable pageable);

    FileDTO fetchMinioFile(String username);
}
