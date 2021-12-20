package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long>{

    void setVote(long userId, long questionId, VoteType vote);
    boolean validateUserVote(Long id, Long userId);
    int getVote(Long id);
}
