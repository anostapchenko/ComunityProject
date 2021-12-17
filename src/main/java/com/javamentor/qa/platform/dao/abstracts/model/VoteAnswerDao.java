package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer, Long> {
    long getVoteCount(long answerId);
    boolean existsVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId);
    VoteAnswer getVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId);
    void addVoteAnswer(VoteType vote, long answerId, User currentUser);
    void updateVoteAnswer(VoteAnswer voteAnswer, long answerId, long currentUserId);
}
