package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateReputationByAnswerIdAndSenderId(int count, long answerId, long senderId) {
        entityManager.createQuery(
                "UPDATE Reputation r SET r.count = r.count + :deltaCount WHERE r.answer.id = :answerId AND r.sender.id = :currentUserId"
        )
                .setParameter("deltaCount", count)
                .setParameter("answerId", answerId)
                .setParameter("currentUserId", senderId)
                .executeUpdate();
    }
}
