package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;

public interface VoteQuestionDao extends ReadWriteDao<VoteQuestion, Long>{

    boolean isUserVoteByQuestionIdAndUserId(Long id, Long userId);
    int getVote(Long id);
}
