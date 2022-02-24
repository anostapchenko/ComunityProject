package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionViewedDaoImpl extends ReadWriteDaoImpl<QuestionViewed, Long> implements QuestionViewedDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Cacheable(value = "QuestionViewed", key = "#questionId+#email")
    public List<QuestionViewed> getQuestionViewedByUserAndQuestion(String email, Long questionId) {
        return entityManager.createQuery(
                        "select q from QuestionViewed q where q.user.email =:email and q.question.id =:questionId", QuestionViewed.class)
                .setParameter("email", email)
                .setParameter("questionId", questionId)
                .getResultList();
    }

    @Override
    @CacheEvict(value = "QuestionViewed", key = "#questionViewed.question.id+#questionViewed.user.email")
    public void persist(QuestionViewed questionViewed) {
        super.persist(questionViewed);
    }
}
