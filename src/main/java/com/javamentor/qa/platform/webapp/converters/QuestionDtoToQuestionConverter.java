package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class QuestionDtoToQuestionConverter {

    public QuestionDtoToQuestionConverter INSTANCE = Mappers.getMapper(QuestionDtoToQuestionConverter.class );

    @Mapping(source = "question.title", target = "title")
    @Mapping(source = "question.description", target = "description")
    @Mapping(source = "question.tags", target = "listTagDto")
    public abstract Question questionDtoToQuestion(QuestionDto questionDto);
}
