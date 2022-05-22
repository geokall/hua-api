package com.hua.api.service;

import com.hua.api.dto.StudentDTO;
import com.hua.api.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Long createStudent(StudentDTO studentDTO) {
        return null;
    }
}
