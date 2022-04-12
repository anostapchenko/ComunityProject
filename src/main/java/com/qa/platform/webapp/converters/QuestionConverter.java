package com.qa.platform.webapp.converters;

import com.qa.platform.models.dto.QuestionCreateDto;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.entity.question.Question;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class QuestionConverter {

    @Mapping(source = "tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.nickname", target = "authorName")
    @Mapping(source = "question.user.imageLink", target = "authorImage")
    @Mapping(constant = "0", target = "countAnswer")
    @Mapping(constant = "0", target = "countValuable")
    @Mapping(constant = "0", target = "viewCount")
    public abstract QuestionViewDto questionToQuestionDto(Question question);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    public abstract Question questionDtoToQuestion(QuestionCreateDto questionCreateDto);

}
