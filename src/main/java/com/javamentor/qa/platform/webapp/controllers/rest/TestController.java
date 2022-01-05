package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.service.impl.pagination.DtoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private DtoServiceImpl<AnswerDTO> answerDTODtoService;

    @Autowired
    public TestController(DtoServiceImpl<AnswerDTO> answerDTODtoService) {
        this.answerDTODtoService = answerDTODtoService;
    }

    public ResponseEntity<PageDTO<AnswerDTO>> getUserPagination(){
        return null;
    }
}
