package com.qa.platform.dao.abstracts.dto;

import com.qa.platform.models.dto.QuestionDto;
import com.qa.platform.models.dto.question.QuestionCommentDto;

import java.util.List;
import java.util.Optional;

public interface QuestionDtoDao {
    List<QuestionCommentDto> getQuestionIdComment(Long id);

    Optional<QuestionDto> getQuestionDtoDaoById(Long id, Long userId);
}
