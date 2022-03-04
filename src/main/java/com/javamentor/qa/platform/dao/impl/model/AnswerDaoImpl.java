package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Answer> getAnswerWithAuthor(long answerId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                "SELECT a FROM Answer a JOIN User u ON a.user.id = u.id WHERE a.id = :answerId", Answer.class)
                .setParameter("answerId", answerId));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        String hql = "UPDATE Answer a SET a.isDeleted = true WHERE a.id = :id";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }
}