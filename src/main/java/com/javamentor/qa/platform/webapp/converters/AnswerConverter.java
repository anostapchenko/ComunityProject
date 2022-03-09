package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AnswerConverter {

    private UserService userService;
    private QuestionService questionService;

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "questionId", target = "question", qualifiedByName = "questionIdToQuestion")
    @Mapping(source = "dateAccept", target = "dateAcceptTime")
    @Mapping(constant = "false", target = "isDeleted")
    @Mapping(constant = "false", target = "isDeletedByModerator")
    public abstract Answer answerDTOToAnswer  (AnswerDTO answerDTO);

    @Named("userIdToUser")
    public User userIdToUser (Long userId) {
        return userService.getById(userId).orElseThrow(
                () -> new ConstrainException("Can't find User with id: " + userId));
    }

    @Named("questionIdToQuestion")
    public Question questionIdToQuestion (Long questionId) {
        return questionService.getById(questionId).orElseThrow(
                () -> new ConstrainException("Can't find Question with id: " + questionId));
    }
}
