package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByBodyImpl;
import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByIdImpl;
import com.javamentor.qa.platform.exception.NoSuchDaoException;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerResourceController {

    private final AnswerPageDtoService answerDtoService;

    @Autowired
    public AnswerResourceController(AnswerPageDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }

    @GetMapping("/api/answer/id")
    public ResponseEntity<Object> paginationById(@RequestBody PaginationData data) {
        try {
            data.setDaoName(AnswerPageDtoDaoByIdImpl.class.getSimpleName());
            return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
        } catch (NoSuchDaoException e) {
            return new ResponseEntity<>("Wrong dao name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/answer/html_body")
    public ResponseEntity<Object> paginationByHtmlBody(@RequestBody PaginationData data) {
        try {
            data.setDaoName(AnswerPageDtoDaoByBodyImpl.class.getSimpleName());
            return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
        } catch (NoSuchDaoException e) {
            return new ResponseEntity<>("Wrong dao name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
