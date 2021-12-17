package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnswerResourceController.class)
public class TestAnswerResourceController {

    @Autowired
    MockMvc mockMvc;


}