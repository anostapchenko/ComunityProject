package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import org.springframework.beans.factory.annotation.Autowired;

public interface GlobalSearchDtoService {

    PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q,int items,int page);

}
