package com.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByTitle")
public class SearchByTitle implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.startsWith("title:")){
            return "QuestionPageDtoDaoSearchInTitle";
        }
        return null;
    }
}
