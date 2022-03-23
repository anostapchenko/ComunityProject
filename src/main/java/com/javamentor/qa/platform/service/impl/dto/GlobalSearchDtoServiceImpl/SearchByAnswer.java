package com.javamentor.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.javamentor.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByAnswer")
public class SearchByAnswer implements GlobalSearchParser {
    @Override
    public String parse(String q) {
        if(q.startsWith("answers:")){
            return "QuestionPageDtoDaoByAnswer";
        }
        return null;
    }
}
