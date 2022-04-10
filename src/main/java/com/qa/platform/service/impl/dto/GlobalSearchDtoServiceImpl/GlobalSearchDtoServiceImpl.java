package com.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.qa.platform.dao.impl.pagination.globalsearch.QuestionPageDtoDaoGlobalSearch;
import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.entity.pagination.PaginationData;
import com.qa.platform.service.abstracts.dto.GlobalSearchDtoService;
import com.qa.platform.service.abstracts.dto.GlobalSearchParser;
import com.qa.platform.service.abstracts.dto.QuestionDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchDtoServiceImpl implements GlobalSearchDtoService {

    private final QuestionDtoService questionDtoService;
    private final List<GlobalSearchParser> list;

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q, int items, int page) {
        PaginationData paginationData;
        String data;
        for(GlobalSearchParser parser : list){
            data = parser.parse(q);
            if(data != null){
                paginationData = new PaginationData(page, items, data);
                paginationData.getProps().put("q", q);
                return questionDtoService.getPageDto(paginationData);
            }
        }

        paginationData = new PaginationData(1, items, QuestionPageDtoDaoGlobalSearch.class.getSimpleName());
        paginationData.getProps().put("q", q);

        return questionDtoService.getPageDto(paginationData);
    }

}
