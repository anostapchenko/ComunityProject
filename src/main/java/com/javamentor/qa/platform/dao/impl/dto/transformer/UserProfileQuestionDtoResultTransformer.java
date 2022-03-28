package com.javamentor.qa.platform.dao.impl.dto.transformer;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.hibernate.transform.ResultTransformer;

import java.util.List;
import java.util.stream.Collectors;

public class UserProfileQuestionDtoResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Question question = (Question) tuple[0];
        UserProfileQuestionDto userProfileQuestionDto = new UserProfileQuestionDto();
        userProfileQuestionDto.setQuestionId(question.getId());
        userProfileQuestionDto.setTitle(question.getTitle());
        userProfileQuestionDto.setListTagDto(question.getTags()
                .stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName(), tag.getDescription()))
                .collect(Collectors.toList()));
        userProfileQuestionDto.setPersistDateTime(question.getPersistDateTime());
        userProfileQuestionDto.setCountAnswer((Long) tuple[1]);
        return userProfileQuestionDto;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
