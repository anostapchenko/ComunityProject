package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.mappers.QuestionMapper;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionResourceController {
    @Autowired
    public final QuestionService questionService;

    @GetMapping("api/user/question/{id}")
    public QuestionDto getQuestion (@PathVariable Long id){
        return QuestionMapper.INSTANCE.convertToDto(questionService.getById(id).get());
    }
}
