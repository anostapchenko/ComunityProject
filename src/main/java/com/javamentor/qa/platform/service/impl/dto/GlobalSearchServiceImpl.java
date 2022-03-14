package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchService;
import org.springframework.stereotype.Service;

@Service
public class GlobalSearchServiceImpl implements GlobalSearchService {

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q) {
        return null;
    }

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByTag(String q) {
        return null;
    }
}
