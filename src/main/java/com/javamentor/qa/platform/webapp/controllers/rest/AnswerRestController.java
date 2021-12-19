package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByBodyImpl;
import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByIdImpl;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerRestController {
    private AnswerPageDtoService answerDtoService;

    @Autowired
    public AnswerRestController(AnswerPageDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }

    @GetMapping("/api/answer/id")
    public PageDTO<AnswerDTO> paginationById(@RequestBody PaginationData data) {
        return answerDtoService.getPageDto(data, AnswerPageDtoDaoByIdImpl.class.getName());
    }

    @GetMapping("/api/answer/html_body")
    public PageDTO<AnswerDTO> paginationByHtmlBody(@RequestBody PaginationData data) {
        return answerDtoService.getPageDto(data, AnswerPageDtoDaoByBodyImpl.class.getName());
    }
}
