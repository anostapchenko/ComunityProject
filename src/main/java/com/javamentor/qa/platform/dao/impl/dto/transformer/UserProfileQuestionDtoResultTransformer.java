package com.javamentor.qa.platform.dao.impl.dto.transformer;

import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import org.hibernate.transform.ResultTransformer;
import java.time.LocalDateTime;
import java.util.List;

public class UserProfileQuestionDtoResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        UserProfileQuestionDto userProfileQuestionDto = new UserProfileQuestionDto();
        userProfileQuestionDto.setQuestionId((Long) tuple[0]);
        userProfileQuestionDto.setTitle((String) tuple[1]);
        userProfileQuestionDto.setCountAnswer((Long) tuple[2]);
        userProfileQuestionDto.setPersistDateTime((LocalDateTime) tuple[3]);
        return userProfileQuestionDto;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
