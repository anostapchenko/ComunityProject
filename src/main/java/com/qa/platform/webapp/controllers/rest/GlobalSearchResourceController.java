package com.qa.platform.webapp.controllers.rest;

import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.service.abstracts.dto.GlobalSearchDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "Global Search Resource Controller", description = "Отдельный контроллер для глобального поиска по сайту")
public class GlobalSearchResourceController {

    private final GlobalSearchDtoService globalSearchService;

    @GetMapping("")
    @Operation(summary = "Получение пагинированного списка вопросов по различным условиям ",
            description = "Получение пагинированного списка вопросов в глобальном поиске " +
                          "В запросе указываем page - номер страницы, " +
                          "items (по умолчанию 10) - количество результатов на странице, " +
                          "не обязательный на фронте")
    @ApiResponse(responseCode = "200", description = "Возвращает пагинированный список PageDTO<QuestionViewDTO> (id, title, authorId," +
            " authorReputation, authorName, authorImage, description, viewCount, countAnswer, countValuable," +
            " LocalDateTime, LocalDateTime, listTagDto", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<PageDTO<QuestionViewDto>> globalSearchByQuestion(@RequestParam int page,
                                                                           @RequestParam String q,
                                                                           @RequestParam(required = false, defaultValue = "10") int items) {

        return new ResponseEntity<>(globalSearchService.getListQuestionDtoByParam(q,items,page), HttpStatus.OK);
    }
}
