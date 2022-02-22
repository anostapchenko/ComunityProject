package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long>{

    boolean validateUserVoteByQuestionIdAndUserId(Long questionId, Long userId);
    Long getVoteByQuestionId(Long questionId);
    void persist(VoteQuestion voteQuestion);
}
