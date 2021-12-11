package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public QuestionDto getQuestionDtoDaoById(Long id) {
        TypedQuery<QuestionDto> q = entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto(b.id, b.title, b.persistDateTime, b.lastUpdateDateTime) FROM Question b WHERE b.id =: id", QuestionDto.class);
        q.setParameter("id", id);

        QuestionDto questionDto = q.getSingleResult();
        return questionDto;
    }
}
