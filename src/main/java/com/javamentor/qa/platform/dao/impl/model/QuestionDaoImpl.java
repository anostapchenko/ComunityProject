package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Long> getCountQuestion() {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("" +
                "SELECT COUNT(q.id) FROM Question q where q.isDeleted=false", Long.class));
    }

    @Override
    public Optional<Question> getQuestionByIdWithAuthor(Long id){
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                "select q from Question q inner join User u on q.user.id = u.id where q.id=:id",Question.class)
                .setParameter("id", id));
    }
}