package com.javamentor.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByViews")
public class SearchByViews implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.startsWith("views:")){
            return "QuestionPageDtoDaoByViews";
        }
        return null;
    }
}
