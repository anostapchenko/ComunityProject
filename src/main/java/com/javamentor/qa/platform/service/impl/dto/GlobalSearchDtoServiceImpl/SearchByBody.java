package com.javamentor.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByBody")
public class SearchByBody implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.startsWith("body:")){
            return "QuestionPageDtoDaoSearchInBody";
        }
        return null;
    }
}
