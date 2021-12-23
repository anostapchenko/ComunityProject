package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long>{

    boolean validateUserVote(Long id, Long userId);
    Long getVote(Long id);
    void persistVoteAndReputation(VoteQuestion voteQuestion, Question question, User user, int count, User authorQuestion);
}
