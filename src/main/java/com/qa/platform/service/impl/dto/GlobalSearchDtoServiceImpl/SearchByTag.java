package com.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByTag")
public class SearchByTag implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.matches("\\[.*]")){
            return "QuestionPageDtoDaoByTagName";
        }
        return null;
    }
}
