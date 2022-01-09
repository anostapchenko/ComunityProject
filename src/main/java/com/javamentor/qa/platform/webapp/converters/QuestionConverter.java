package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
//@Mapper(componentModel = "spring")
public abstract class QuestionConverter {

    @Mapping(source = "tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.nickname", target = "authorName")
    @Mapping(source = "question.user.imageLink", target = "authorImage")
    @Mapping(constant = "0", target = "countAnswer")
    @Mapping(constant = "0", target = "countValuable")
    @Mapping(constant = "0", target = "viewCount")
    public abstract QuestionDto questionToQuestionDto(Question question);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    public abstract Question questionDtoToQuestion(QuestionCreateDto questionCreateDto);

}
