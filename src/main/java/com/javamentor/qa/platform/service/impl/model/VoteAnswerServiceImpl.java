package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private VoteAnswerDao voteAnswerDao;
    private ReputationDao reputationDao;

    @Autowired
    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao,
                                 ReputationDao reputationDao) {
        super(voteAnswerDao);
        this.voteAnswerDao = voteAnswerDao;
        this.reputationDao = reputationDao;
    }

    @Override
    public long getVoteCount(long answerId) {
        return voteAnswerDao.getVoteCount(answerId);
    }

    @Override
    public boolean existsVoteByAnswerAndUser(long answerId, long currentUserId) {
        return voteAnswerDao.existsVoteByAnswerAndUser(answerId, currentUserId);
    }

    public void persist(VoteAnswer voteAnswer) {
        int count = -5;
        if (voteAnswer.getVote().equals(VoteType.UP_VOTE)) {
            count = 10;
        }
        voteAnswerDao.persist(voteAnswer);
        reputationDao.persist(Reputation.builder()
                .count(count)
                .persistDate(Timestamp.from(Instant.now()).toLocalDateTime())
                .type(ReputationType.VoteAnswer)
                .answer(voteAnswer.getAnswer())
                .author(voteAnswer.getAnswer().getUser())
                .sender(voteAnswer.getUser())
                .build());
    }
}
