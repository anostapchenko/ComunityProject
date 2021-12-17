package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;

@Transactional
@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;
    private AnswerService answerService;
    private ReputationDao reputationDaoImpl;

    @Autowired
    public void setUserService(AnswerService answerService,
                               ReputationDao reputationDaoImpl) {
        this.answerService = answerService;
        this.reputationDaoImpl = reputationDaoImpl;
    }

    @Override
    public long getVoteCount(long answerId) {
        Query query = entityManager.createNativeQuery(
                "SELECT SUM(" +
                        "(SELECT COUNT(*) FROM votes_on_answers WHERE votes_on_answers.answer_id = :answerId AND votes_on_answers.vote = :upVote)" +
                        " - (SELECT COUNT(*) FROM votes_on_answers WHERE votes_on_answers.answer_id = :answerId AND votes_on_answers.vote = :downVote)" +
                        ")")
                .setParameter("answerId", answerId)
                .setParameter("upVote", VoteType.UP_VOTE.toString())
                .setParameter("downVote", VoteType.DOWN_VOTE.toString());
        return ((BigDecimal) query.getSingleResult()).longValue();
    }

    @Override
    public boolean existsVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        BigInteger count = (BigInteger) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM votes_on_answers WHERE answer_id = :answerId AND user_id = :currentUserId"
        )
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult();
        return count.compareTo(BigInteger.ZERO) > 0;
    }

    @Override
    public VoteAnswer getVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return (VoteAnswer) entityManager.createNativeQuery(
                "SELECT * FROM votes_on_answers WHERE answer_id = :answerId AND user_id = :currentUserId",
                VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult();
    }

    @Override
    public void addVoteAnswer(VoteType vote, long answerId, User currentUser) {
        int count = -5;
        if (vote.equals(VoteType.UP_VOTE)) count = 10;
        User author = (User) entityManager.createNativeQuery(
                "SELECT * FROM user_entity WHERE user_entity.id = " +
                        "(SELECT answer.user_id FROM answer WHERE answer.id = :answerId)",
                User.class)
                .setParameter("answerId", answerId)
                .getSingleResult();
        Answer answer = answerService.getById(answerId).get();
        persist(new VoteAnswer(currentUser, answer, vote));
        reputationDaoImpl.persist(Reputation.builder()
                .count(count)
                .persistDate(Timestamp.from(Instant.now()).toLocalDateTime())
                .type(ReputationType.VoteAnswer)
                .answer(answer)
                .author(author)
                .sender(currentUser)
                .build());
    }

    @Override
    public void updateVoteAnswer(VoteAnswer voteAnswer, long answerId, long currentUserId) {
        int deltaCount = -15;
        if (voteAnswer.getVote().equals(VoteType.UP_VOTE)) deltaCount = 15;
        update(voteAnswer);
        entityManager.createNativeQuery(
                "UPDATE reputation SET count = count + :deltaCount WHERE answer_id = :answerId AND sender_id = :currentUserId"
        )
                .setParameter("deltaCount", deltaCount)
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .executeUpdate();
    }
}