package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private VoteAnswerDao voteAnswerDao;
    private ReputationDao reputationDao;
    private AnswerDao answerDao;

    @Autowired
    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao,
                                 ReputationDao reputationDao,
                                 AnswerDao answerDao) {
        super(voteAnswerDao);
        this.voteAnswerDao = voteAnswerDao;
        this.reputationDao = reputationDao;
        this.answerDao = answerDao;
    }

    @Override
    public long getVoteCount(long answerId) {
        return voteAnswerDao.getVoteCount(answerId);
    }

    @Override
    public boolean existsVoteByAnswerAndUser(long answerId, long currentUserId) {
        return voteAnswerDao.existsVoteByAnswerAndUser(answerId, currentUserId);
    }

    @Transactional
    @Override
    public void addVoteAnswer(VoteType vote, long answerId, User currentUser) {
        int count = -5;
        if (vote.equals(VoteType.UP_VOTE)) count = 10;
        Object[] list = answerDao.getUser0AndAnswer1ByAnswerId(answerId);
        voteAnswerDao.persist(new VoteAnswer(currentUser, (Answer) list[1], vote));
        reputationDao.persist(Reputation.builder()
                .count(count)
                .persistDate(Timestamp.from(Instant.now()).toLocalDateTime())
                .type(ReputationType.VoteAnswer)
                .answer((Answer) list[1])
                .author((User) list[0])
                .sender(currentUser)
                .build());
    }
}
