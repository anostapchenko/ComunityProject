package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchDtoService;
import org.springframework.stereotype.Service;

@Service
public class GlobalSearchDtoServiceImpl implements GlobalSearchDtoService {

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q) {
        if (q.matches(":")){
            String[] values = q.split(":");

        } else if (q.matches("\\+")){

        } else if (q.matches("\"")){

        }
        return null;
    }

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByTag(String q) {
        return null;
    }
}
