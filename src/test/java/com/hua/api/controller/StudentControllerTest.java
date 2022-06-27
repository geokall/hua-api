package com.hua.api.controller;

import com.hua.api.dto.StudentDTO;
import com.hua.api.service.StudentService;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

    @MockBean
    private StudentService studentService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void findStudent() throws Exception {
        var dto = new StudentDTO();
        dto.setId(1L);
        when(studentService.findStudent(anyLong()))
                .thenReturn(dto);

        mvc.perform(MockMvcRequestBuilders.get("/api/student/find/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("id", is(1)));
    }
}