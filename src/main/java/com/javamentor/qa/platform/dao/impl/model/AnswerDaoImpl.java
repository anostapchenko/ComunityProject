package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

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

    @Override
    public boolean existsByAnswerIdAndUserIdAndQuestionId(Long answerId, Long userId, Long questionId) {
        return (boolean) entityManager.createQuery(
                        "SELECT COUNT(a) > 0 FROM Answer a " +
                                "WHERE a.id =: answerId AND a.user.id = :userId" +
                                " AND a.question.id = :questionId")
                .setParameter("answerId", answerId)
                .setParameter("userId", userId)
                .setParameter("questionId", questionId)
                .getSingleResult();

    }

    @Override
    public void deleteById(Long id) {
        String hql = "UPDATE Answer a SET a.isDeleted = true WHERE a.id = :id";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }
}