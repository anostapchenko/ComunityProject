package com.javamentor.qa.platform.webapp.controllers.question;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "QuestionCount", description = "Количество всего вопросов в бд")
@RestController
public class QuestionResouceController {
    final private QuestionService questionService;

    public QuestionResouceController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("api/user/question/count")
    @Operation(summary = "Количество всего вопросов в бд")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))
    })
    @ApiResponse(responseCode = "400", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json")
    })

    public ResponseEntity<Long> getUserDtoId() {
        Long countQusetion = questionService.getCountByQuestion();
        return new ResponseEntity<>(countQusetion, HttpStatus.OK);
    }

}
