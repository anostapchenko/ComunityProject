package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;

public interface GlobalSearchService {

    PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q);

    PageDTO<QuestionViewDto> getListQuestionDtoByTag(String q);
}
