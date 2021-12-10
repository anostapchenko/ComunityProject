package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionConverter {
    QuestionConverter INSTANCE = Mappers.getMapper(QuestionConverter.class);
    QuestionDto questionToDto(Question question);

}
