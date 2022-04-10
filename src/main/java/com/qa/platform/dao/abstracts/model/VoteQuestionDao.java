package com.qa.platform.dao.abstracts.model;

import com.qa.platform.models.entity.question.VoteQuestion;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long>{

    boolean isUserNotVoteByQuestionIdAndUserId(Long id, Long userId);
    Long getVoteByQuestionId(Long questionId);
}
