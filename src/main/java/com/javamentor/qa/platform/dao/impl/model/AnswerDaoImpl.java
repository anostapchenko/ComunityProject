package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Object[] getUser0AndAnswer1ByAnswerId(long answerId) {
        return entityManager.createQuery(
                "SELECT u, a FROM User u JOIN Answer a ON u.id = a.user.id WHERE a.id = :answerId",
                Object[].class)
                .setParameter("answerId", answerId)
                .getSingleResult();
    }
}