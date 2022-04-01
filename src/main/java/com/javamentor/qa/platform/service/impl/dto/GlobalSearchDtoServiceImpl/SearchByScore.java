package com.javamentor.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByScore")
public class SearchByScore implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.startsWith("score:")){
            return "QuestionPageDtoDaoByScore";
        }
        return null;
    }
}
