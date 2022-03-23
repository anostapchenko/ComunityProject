package com.javamentor.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.javamentor.qa.platform.dao.impl.pagination.globalsearch.QuestionPageDtoDaoByTagName;
import com.javamentor.qa.platform.dao.impl.pagination.globalsearch.QuestionPageDtoDaoGlobalSearch;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchParser;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchDtoServiceImpl implements GlobalSearchDtoService {

    private final QuestionDtoService questionDtoService;
    private final List<GlobalSearchParser> list;

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByParam(String q,int items) {
        PaginationData paginationData;
        String data;
        for(GlobalSearchParser parser : list){
            data = parser.parse(q);
            if(data != null){
                paginationData = new PaginationData(1, items, data);
                paginationData.getProps().put("q", q);
                return questionDtoService.getPageDto(paginationData);
            }
        }

        paginationData = new PaginationData(1, items, QuestionPageDtoDaoGlobalSearch.class.getSimpleName());
        paginationData.getProps().put("q", q);

        return questionDtoService.getPageDto(paginationData);
    }

    @Override
    public PageDTO<QuestionViewDto> getListQuestionDtoByTagName(String tagName,int items) {

        List<String> tagsList = new ArrayList<>();
        List<String> ignoreTags = new ArrayList<>();

        for(String s : Arrays.asList(tagName.split("\\+"))){
            if(s.startsWith("-")){
                ignoreTags.add(s.substring(1));
            } else {
                tagsList.add(s);
            }
        }

        PaginationData data = new PaginationData(1,items, QuestionPageDtoDaoByTagName.class.getSimpleName());
        data.getProps().put("tags",tagsList);
        data.getProps().put("ignoreTags",ignoreTags);
        return questionDtoService.getPageDto(data);
    }

}
