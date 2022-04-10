package com.qa.platform.dao.impl.dto.transformer;

import com.qa.platform.models.dto.QuestionDto;
import com.qa.platform.models.entity.question.answer.VoteType;
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
        questionDto.setCountValuable(tuple[10] == null ? 0 : ((Number) tuple[10]).intValue());
        questionDto.setIsUserVote(tuple[11] == null ? null : (VoteType) tuple[11]);
        return questionDto;
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
