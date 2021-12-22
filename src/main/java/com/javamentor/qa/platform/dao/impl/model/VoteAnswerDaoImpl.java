package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    private ReputationDao reputationDaoImpl;

    @Autowired
    public void setUserService(ReputationDao reputationDaoImpl) {
        this.reputationDaoImpl = reputationDaoImpl;
    }

    @Transactional
    @Override
    public long getVoteCount(long answerId) {
        return entityManager.createQuery(
                "select sum(case when vote = :upVote then 1 else -1 end) as diff from VoteAnswer where answer.id = :answerId",
                Long.class
        )
                .setParameter("upVote", VoteType.UP_VOTE)
                .setParameter("answerId", answerId)
                .getSingleResult();
    }

    @Override
    public boolean existsVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return entityManager.createQuery(
                "SELECT COUNT(v) FROM VoteAnswer v WHERE v.answer.id = :answerId AND  v.user.id = :currentUserId",
                Long.class
        )
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult() > 0;
    }

    @Override
    public VoteAnswer getVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return entityManager.createQuery(
                "SELECT v FROM VoteAnswer v WHERE v.answer.id = :answerId AND v.user.id = :currentUserId",
                VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult();
    }

    @Transactional
    @Override
    public void addVoteAnswer(VoteType vote, long answerId, User currentUser) {
        int count = -5;
        if (vote.equals(VoteType.UP_VOTE)) count = 10;
        Object[] list = entityManager.createQuery(
                "SELECT u, a FROM User u JOIN Answer a ON u.id = a.user.id WHERE a.id = :answerId",
                Object[].class)
                .setParameter("answerId", answerId)
                .getSingleResult();
        persist(new VoteAnswer(currentUser, (Answer)list[1], vote));
        reputationDaoImpl.persist(Reputation.builder()
                .count(count)
                .persistDate(Timestamp.from(Instant.now()).toLocalDateTime())
                .type(ReputationType.VoteAnswer)
                .answer((Answer)list[1])
                .author((User)list[0])
                .sender(currentUser)
                .build());
    }

    @Transactional
    @Override
    public void updateVoteAnswer(VoteAnswer voteAnswer, long answerId, long currentUserId) {
        int deltaCount = -15;
        if (voteAnswer.getVote().equals(VoteType.UP_VOTE)) deltaCount = 15;
        update(voteAnswer);
        entityManager.createQuery(
                "UPDATE Reputation r SET r.count = r.count + :deltaCount WHERE r.answer.id = :answerId AND r.sender.id = :currentUserId"
        )
                .setParameter("deltaCount", deltaCount)
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate();
    }
}