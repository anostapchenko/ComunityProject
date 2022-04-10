package com.qa.platform.dao.abstracts.model;

import com.qa.platform.models.entity.question.answer.VoteAnswer;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer, Long> {
    long getVoteCount(long answerId);
    boolean existsVoteByAnswerAndUser(long answerId, long currentUserId);
}
