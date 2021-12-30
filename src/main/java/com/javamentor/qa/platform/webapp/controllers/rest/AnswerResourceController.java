package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByBodyImpl;
import com.javamentor.qa.platform.dao.impl.pagination.AnswerPageDtoDaoByIdImpl;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/answer")
public class AnswerResourceController {

    private final AnswerPageDtoService answerDtoService;

    @Autowired
    public AnswerResourceController(AnswerPageDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }

    @GetMapping("/id")
    public ResponseEntity<PageDTO<AnswerDTO>> paginationById(@RequestParam int page, @RequestParam int items) {
        PaginationData data = new PaginationData(page, items,
                AnswerPageDtoDaoByIdImpl.class.getSimpleName());
        return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);

    }

    @GetMapping("/html_body")
    public ResponseEntity<PageDTO<AnswerDTO>> paginationByHtmlBody(@RequestParam int page, @RequestParam int items) {
        PaginationData data = new PaginationData(page, items,
                AnswerPageDtoDaoByBodyImpl.class.getSimpleName());
        return new ResponseEntity<>(answerDtoService.getPageDto(data), HttpStatus.OK);
    }
}
