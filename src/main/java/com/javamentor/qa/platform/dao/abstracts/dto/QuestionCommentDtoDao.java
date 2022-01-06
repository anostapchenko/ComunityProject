package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;

import java.util.List;

public interface QuestionCommentDtoDao {
    List<QuestionCommentDto> getQuestionIdComment(Long id);
}
