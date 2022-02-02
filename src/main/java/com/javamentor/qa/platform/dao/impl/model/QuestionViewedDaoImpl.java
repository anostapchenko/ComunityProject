package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionViewed> getQuestionViewedByUserAndQuestion(Long userId, Long questionId) {
        return entityManager.createQuery(
                        "select q from QuestionViewed q where q.user.id =:userId and q.question.id =:questionId", QuestionViewed.class)
                .setParameter("userId", userId)
                .setParameter("questionId", questionId)
                .getResultList();
    }
}
