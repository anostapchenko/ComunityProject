package com.javamentor.qa.platform.dao.impl.pagination.transformer;

import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import org.hibernate.transform.ResultTransformer;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionPageDtoResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] strings) {
        QuestionViewDto questionViewDto = new QuestionViewDto();
        questionViewDto.setId((Long) tuple[0]);
        questionViewDto.setTitle((String) tuple[1]);
        questionViewDto.setAuthorId((Long) tuple[2]);
        questionViewDto.setAuthorName((String) tuple[3]);
        questionViewDto.setAuthorImage((String) tuple[4]);
        questionViewDto.setDescription((String) tuple[5]);
        questionViewDto.setPersistDateTime((LocalDateTime) tuple[6]);
        questionViewDto.setLastUpdateDateTime((LocalDateTime) tuple[7]);
        questionViewDto.setAuthorReputation((Long) tuple[8]);
        questionViewDto.setCountAnswer(((Number) tuple[9]).intValue());
        questionViewDto.setCountValuable(((Number) tuple[10]).intValue());
        return questionViewDto;
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
