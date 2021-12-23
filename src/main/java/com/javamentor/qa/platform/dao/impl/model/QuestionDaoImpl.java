package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Long getCountQuestion() {
        return (Long) entityManager.createQuery("SELECT COUNT(q.id) FROM Question q where q.isDeleted=false")
                .getSingleResult();
    }
}