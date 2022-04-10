package com.qa.platform.service.abstracts.dto;

import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.dto.QuestionViewDto;

public interface GlobalSearchDtoService {

    PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q, int items, int page);

}
