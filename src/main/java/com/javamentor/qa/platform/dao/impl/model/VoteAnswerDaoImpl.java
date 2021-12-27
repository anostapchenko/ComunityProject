package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

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
    public boolean existsVoteByAnswerAndUser(long answerId, long currentUserId) {
        return entityManager.createQuery(
                "SELECT COUNT(v.id) FROM VoteAnswer v WHERE v.answer.id = :answerId AND v.user.id = :currentUserId",
                Long.class
        )
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", currentUserId)
                .getSingleResult() > 0;
    }
}