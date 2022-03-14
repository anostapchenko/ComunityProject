package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.QuestionPageDtoDaoByTagName;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
@Tag(name = "Global Search Resource Controller", description = "Отдельный контроллер для глобального поиска по сайту")
public class GlobalSearchResourceController {

    private GlobalSearchService globalSearchService;
    private QuestionDtoService questionDtoService;

    @GetMapping("")
    public ResponseEntity<PageDTO<QuestionViewDto>> globalSearchByQuestion(@RequestParam String q){

        return new ResponseEntity<>(globalSearchService.getListQuestionDtoByParam(q), HttpStatus.OK);
    }

    @GetMapping("/tagged/{tag}")
    public ResponseEntity<PageDTO<QuestionViewDto>> globalSearchByTag(@PathVariable("tag") String tag){

        PaginationData data = new PaginationData(1,10, QuestionPageDtoDaoByTagName.class.getSimpleName());
        data.getProps().put("tags",tag.split("/+"));

        return new ResponseEntity<>(questionDtoService.getPageDto(data), HttpStatus.OK);
    }
}
