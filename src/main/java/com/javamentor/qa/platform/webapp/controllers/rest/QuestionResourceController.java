package com.javamentor.qa.platform.webapp.controllers.rest;



import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.impl.dto.QuestionDtoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionResourceController {
    @Autowired
    public final QuestionService questionService;
    public final TagService tagService;
    public final QuestionDtoServiceImpl questionDtoService;

    @GetMapping("api/user/question/{id}")
    public QuestionDto getQuestion(@PathVariable Long id) {
        return questionDtoService.getQuestionDtoServiceById(id);
    }
}
