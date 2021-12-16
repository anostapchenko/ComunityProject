package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.impl.model.TestFakeReputationData;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public QuestionDto getQuestionDtoDaoById(Long id) {
        TypedQuery<QuestionDto> q = entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.QuestionDto(q.id, " +
                        "q.title, u.id, u.fullName, u.imageLink, q.description," +
                        "q.persistDateTime, q.lastUpdateDateTime)" +
                        " FROM Question q JOIN  q.user u  WHERE q.id =: id ", QuestionDto.class);
        q.setParameter("id", id);
        QuestionDto questionDto = q.getSingleResult();
        Integer ID = (Integer) entityManager.createQuery("SELECT r.count FROM Reputation r WHERE r.id =: id")
                .setParameter("id", id)
                .getSingleResult();
// Пытаюсь взять объект Reputation из БД или через find или SQL запросом, выходит исключение java.lang.ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 4
//        Reputation rep = entityManager.find(Reputation.class,2L);
//        Query query = entityManager.createQuery("select r from Reputation r WHERE r.id =: id", Reputation.class).setParameter("id", 2L);
//        query.getResultList();
//
        questionDto.setAuthorReputation(new Long(ID)); // тестовые данные в таблицу Reputation заводил в ручную
        questionDto.setViewCount(0); //(пока не считай это поле, как оно будет считаться решим позже, пусть пока будет 0)
        questionDto.setCountAnswer(333);  // не понимаю, где брать и как считать
        questionDto.setCountValuable(444);  // не понимаю, где брать и как считать
        return questionDto;
    }
}
