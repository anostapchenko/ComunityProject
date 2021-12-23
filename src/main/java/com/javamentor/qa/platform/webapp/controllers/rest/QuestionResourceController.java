package com.javamentor.qa.platform.webapp.controllers.rest;



import com.javamentor.qa.platform.models.dto.AuthenticationResponse;
import com.javamentor.qa.platform.models.dto.QuestionResponce;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.impl.dto.QuestionDtoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User question information", description = "Информация по вопросу пользователя")
public class QuestionResourceController {

    public final QuestionDtoService questionDtoService;

    public QuestionResourceController(QuestionDtoService questionDtoService) {
        this.questionDtoService = questionDtoService;
    }

    @GetMapping("/question/{id}")
    @Operation(summary = "Получение информации по вопросу пользователя")
    @ApiResponse(responseCode = "200", description = "Информация по вопросу", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponce.class))
    })
    @ApiResponse(responseCode = "500", description = "Не верный номер вопроса", content = {
            @Content(mediaType = "application/json")
    })
    public ResponseEntity<Object> getQuestion(@PathVariable Long id){
        QuestionDto questionDto = questionDtoService.getQuestionDtoServiceById(id);
        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }
}
