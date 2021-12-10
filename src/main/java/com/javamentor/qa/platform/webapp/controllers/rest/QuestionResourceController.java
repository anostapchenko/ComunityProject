package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.mappers.QuestionMapper;
import com.javamentor.qa.platform.mappers.TagMapper;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
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

    @GetMapping("api/user/question/{id}")
    public QuestionDto getQuestion(@PathVariable Long id) {
        QuestionDto questionDto = QuestionMapper.INSTANCE.questionToDto(questionService.getById(id).get());
        TagDto tagDto = TagMapper.INSTANCE.tagToDto(tagService.getById(id).get());
        Question question = questionService.getById(id).get();
        User user = questionService.getById(id).get().getUser();
        List<TagDto> list = new ArrayList<>();
        list.add(tagDto);
        questionDto.setListTagDto(list);
        questionDto.setAuthorName(user.getFullName());
        questionDto.setAuthorId(user.getId());
        questionDto.setAuthorReputation(0l);  //(можно подсчитать с помощью sql);
        questionDto.setAuthorImage(user.getImageLink());
        questionDto.setViewCount(0); //(пока не считай это поле, как оно будет считаться решим позже, пусть пока будет 0)
        questionDto.setCountAnswer(0); // (можно подсчитать с помощью sql);
        questionDto.setCountValuable(0); // (Это голоса за ответ QuiestionVote);
        return questionDto;
    }
}
