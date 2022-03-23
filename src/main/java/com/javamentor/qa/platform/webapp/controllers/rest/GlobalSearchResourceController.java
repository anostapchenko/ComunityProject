package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.globalsearch.QuestionPageDtoDaoByTagName;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "Global Search Resource Controller", description = "Отдельный контроллер для глобального поиска по сайту")
public class GlobalSearchResourceController {

    private final GlobalSearchDtoService globalSearchService;

    @GetMapping("")
    @Operation(summary = "Получение пагинированного списка вопросов по различным условиям ",
            description = "Получение пагинированного списка вопросов в глобальном поиске" +
                          "items (по умолчанию 10) - количество результатов на странице," +
                          "не обязательный на фронте")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionViewDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> globalSearchByQuestion(@RequestParam String q,
                                                                           @RequestParam(required = false, defaultValue = "10") int items) {

        return new ResponseEntity<>(globalSearchService.getListQuestionDtoByParam(q,items), HttpStatus.OK);
    }

    @GetMapping("tagged/{tag}")
    @Operation(summary = "Получение пагинированного списка вопросов по имени тэга ",
            description = "Получение пагинированного списка вопросов в глобальном поиске, " +
                    "в запросе указываем tag(обязательный параметр) - имена тэгов разделённых +, если их несколько," +
                    "если тэг нужно игнорить то указываем минус перед названием, items (по умолчанию 10) - количество результатов на странице," +
                    "не обязательный на фронте")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionViewDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> globalSearchByTag(@PathVariable String tag,
                                                                      @RequestParam(required = false, defaultValue = "10") int items) {

        return new ResponseEntity<>(globalSearchService.getListQuestionDtoByTagName(tag,items), HttpStatus.OK);
    }
}
