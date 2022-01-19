package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;

import java.util.List;
import java.util.Optional;

public interface QuestionDtoDao {
    List<QuestionCommentDto> getQuestionIdComment(Long id);

    Optional<QuestionDto> getQuestionDtoDaoById(Long id);
}
