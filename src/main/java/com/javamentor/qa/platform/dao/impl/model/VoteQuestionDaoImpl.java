package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion,Long> implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isUserNotVoteByQuestionIdAndUserId(Long id, Long userId){
        return entityManager.createQuery("select count (v.id) from VoteQuestion v where (v.user.id=:userId) and (v.question.id=: id )", Long.class)
                .setParameter("userId", userId)
                .setParameter("id", id)
                .getSingleResult() == 0;
    }
@Override
public Long getVoteByQuestionId(Long questionId) {
    return entityManager.createQuery(
                    "select count(v.id) from VoteQuestion v where v.question.id=:ID", Long.class)
            .setParameter("ID", questionId)
            .getSingleResult();
}
}
