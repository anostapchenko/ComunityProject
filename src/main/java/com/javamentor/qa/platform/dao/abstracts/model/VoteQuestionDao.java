package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long>{

    boolean isUserNotVoteByQuestionIdAndUserId(Long id, Long userId);
    Long getVoteByQuestionId(Long questionId);
}
