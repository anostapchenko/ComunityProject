package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.stereotype.Service;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private VoteAnswerDao voteAnswerDaoImpl;

    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDaoImpl) {
        super(voteAnswerDaoImpl);
        this.voteAnswerDaoImpl = voteAnswerDaoImpl;
    }

    @Override
    public long getVoteCount(long answerId) {
        return voteAnswerDaoImpl.getVoteCount(answerId);
    }

    @Override
    public boolean existsVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return voteAnswerDaoImpl.existsVoteAnswerByAnswerIdAndUserId(answerId, currentUserId);
    }

    @Override
    public VoteAnswer getVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return voteAnswerDaoImpl.getVoteAnswerByAnswerIdAndUserId(answerId, currentUserId);
    }

    @Override
    public void addVoteAnswer(VoteType vote, long answerId, User currentUser) {
        voteAnswerDaoImpl.addVoteAnswer(vote, answerId, currentUser);
    }

    @Override
    public void updateVoteAnswer(VoteAnswer voteAnswer, long answerId, long currentUserId) {
        voteAnswerDaoImpl.updateVoteAnswer(voteAnswer, answerId, currentUserId);
    }
}
