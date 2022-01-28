package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface QuestionDtoService extends PageDtoService<QuestionDto>  {

    Optional<QuestionDto> getQuestionDtoServiceById(Long id);

    List<QuestionCommentDto> getQuestionByIdComment(Long id);

    Map<Long, List<TagDto>> getTagsByQuestionIds(List<Long> questionIds);

    PageDTO<QuestionDto> getQuestionsSortedByDate(PaginationData data);
}
