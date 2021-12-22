package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long>{

    boolean validateUserVote(Long id, Long userId);
    Long getVote(Long id);
    void setVoteAndSetReputation(long userId, long questionId, VoteType vote, int count);
}
