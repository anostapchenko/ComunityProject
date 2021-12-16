package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;

public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;
    private AnswerService answerService;

    @Autowired
    public void setUserService(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Override
    public long getVoteCount(long answerId) {
        return (long) entityManager.createNativeQuery(
                "SELECT SUM(" +
                        "(SELECT COUNT(*) FROM votes_on_answers WHERE answer_id = :answerId AND vote = " + VoteType.UP_VOTE + ")" +
                        " - (SELECT COUNT(*) FROM votes_on_answers WHERE answer_id = :answerId AND vote = " + VoteType.DOWN_VOTE + ")" +
                        ")"
        )
                .setParameter("answerId", answerId)
                .getSingleResult();
    }

    @Override
    public boolean existsVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        long count = (long) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM votes_on_answers WHERE answer_id = :answerId AND user_entity.id = :currentUserId"
        )
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public VoteAnswer getVoteAnswerByAnswerIdAndUserId(long answerId, long currentUserId) {
        return (VoteAnswer) entityManager.createNativeQuery(
                "SELECT * FROM VoteAnswer WHERE Answer.id = :answerId AND User.id = :currentUserId"
        )
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult();
    }

    @Override
    public void addVoteAnswer(VoteType vote, long answerId, User currentUser) {
        int count = -5;
        if (vote.equals(VoteType.UP_VOTE)) count = 10;
        User author = (User)entityManager.createNativeQuery(
                "SELECT * FROM user WHERE user.id = " +
                        "(SELECT answer.user_id FROM answer WHERE answer.id = :answerId)"
        )
                .setParameter("answerId", answerId)
                .getSingleResult();
        Answer answer = answerService.getById(answerId).get();
        persist(new VoteAnswer(currentUser, answer, vote));
        entityManager.createQuery(
                "INSERT INTO reputation (" +
                        "count, persist_date, type, answer_id, sender_id) " +
                        "values (" +
                        count + ", " +
                        Timestamp.from(Instant.now()) + ", " +
                        ReputationType.VoteAnswer + ", " +
                        answerId + ", " +
                        author.getId() + ", " +
                        currentUser.getId() + "))"
        ).executeUpdate();
    }

    @Override
    public void updateVoteAnswer(VoteAnswer voteAnswer, long answerId) {
        int deltaCount = -15;
        if(voteAnswer.getVote().equals(VoteType.UP_VOTE)) deltaCount = 15;
        update(voteAnswer);
        entityManager.createNativeQuery(
                "UPDATE reputation SET count = count - :deltaCount WHERE answer_id = :answerId"
        )
                .setParameter("deltaCount", deltaCount)
                .setParameter("answerId", answerId)
                .executeUpdate();
    }
}