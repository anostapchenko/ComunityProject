package com.qa.platform.service.impl.dto.GlobalSearchDtoServiceImpl;

import com.qa.platform.service.abstracts.dto.GlobalSearchParser;
import org.springframework.stereotype.Component;

@Component("SearchByUser")
public class SearchByUser implements GlobalSearchParser {

    @Override
    public String parse(String q) {
        if(q.startsWith("user:")){
            return "QuestionPageDtoDaoByUserName";
        }
        return null;
    }
}
