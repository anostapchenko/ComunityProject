package com.javamentor.qa.platform.webapp.controllers.rest;



import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.impl.dto.QuestionDtoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionResourceController {

    public final QuestionDtoService questionDtoService;

    public QuestionResourceController(QuestionDtoService questionDtoService) {
        this.questionDtoService = questionDtoService;
    }

    @GetMapping("api/user/question/{id}")
    public ResponseEntity<Object> getQuestion(@PathVariable Long id){
        QuestionDto questionDto = questionDtoService.getQuestionDtoServiceById(id);
        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

}
