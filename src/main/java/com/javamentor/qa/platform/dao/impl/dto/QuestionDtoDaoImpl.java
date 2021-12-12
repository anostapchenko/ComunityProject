package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public QuestionDto getQuestionDtoDaoById(Long id) {

        TypedQuery<QuestionDto> q = entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto(q.id, " +
                        "q.title, u.id, u.fullName, u.imageLink, q.description," +
                        "q.persistDateTime, q.lastUpdateDateTime)" +
                        " FROM Question q JOIN q.user u WHERE q.id =: id ", QuestionDto.class);
        q.setParameter("id", id);

        QuestionDto questionDto = q.getSingleResult();
        questionDto.setAuthorReputation(666l);
        questionDto.setViewCount(555);
        questionDto.setCountAnswer(333);
        questionDto.setCountValuable(444);
        questionDto.setListTagDto(Collections.emptyList());
        return questionDto;
    }
}
