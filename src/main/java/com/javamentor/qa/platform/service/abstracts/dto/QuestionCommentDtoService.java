package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;

import java.util.List;
import java.util.Optional;

public interface QuestionCommentDtoService {
    List<QuestionCommentDto> getQuestionByIdComment(Long id);
}
