package com.hua.api.controller;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void findStudent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/student/find/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("id", is(1)));
    }
}