package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class QuestionCommentDtoDaoImpl implements QuestionCommentDtoDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionCommentDto> getQuestionIdComment(Long id) {
        return entityManager.createQuery(
                "SELECT NEW com.javamentor.qa.platform.models.dto.question.QuestionCommentDto(" +
                        "c.id," +
                        "c.id," +
                        "c.lastUpdateDateTime," +
                        "c.persistDateTime," +
                        "c.text," +
                        "c.id," +
                        "c.id," +
                        "(select sum(r.count) from Reputation r where r.author.id=c.id)) FROM Comment c where c.id=:id")
                .setParameter("id", id)
                .getResultList();
    }
}
