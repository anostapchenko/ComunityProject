package com.javamentor.qa.platform.dao.impl.pagination.transformer;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.transform.ResultTransformer;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDtoResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] strings) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setId((Long) tuple[0]);
        questionDto.setTitle((String) tuple[1]);
        questionDto.setAuthorId((Long) tuple[2]);
        questionDto.setAuthorName((String) tuple[3]);
        questionDto.setAuthorImage((String) tuple[4]);
        questionDto.setDescription((String) tuple[5]);
        questionDto.setPersistDateTime((LocalDateTime) tuple[6]);
        questionDto.setLastUpdateDateTime((LocalDateTime) tuple[7]);
        questionDto.setAuthorReputation((Long) tuple[8]);
        questionDto.setCountAnswer(((Number) tuple[9]).intValue());
        questionDto.setCountValuable(((Number) tuple[10]).intValue());
        return questionDto;
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
