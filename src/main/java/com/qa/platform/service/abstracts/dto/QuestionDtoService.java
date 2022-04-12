package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.QuestionDto;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.dto.question.QuestionCommentDto;

import java.util.List;
import java.util.Optional;


public interface QuestionDtoService extends PageDtoService<QuestionViewDto> {
    Optional<QuestionDto> getQuestionDtoServiceById(Long id);

    List<QuestionCommentDto> getQuestionByIdComment(Long id);

}
