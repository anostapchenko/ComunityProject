package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

public interface VoteAnswerService extends ReadWriteService<VoteAnswer, Long>{

    long getVoteCount(long answerId);
    boolean existsVoteByAnswerAndUser(long answerId, long currentUserId);
    void persist(VoteAnswer voteAnswer);
}
