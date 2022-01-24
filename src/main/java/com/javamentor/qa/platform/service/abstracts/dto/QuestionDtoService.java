package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.List;
import java.util.Optional;


public interface QuestionDtoService extends PageDtoService<QuestionDto>{
    Optional<QuestionDto> getQuestionDtoServiceById(Long id);

    List<QuestionCommentDto> getQuestionByIdComment(Long id);
}
