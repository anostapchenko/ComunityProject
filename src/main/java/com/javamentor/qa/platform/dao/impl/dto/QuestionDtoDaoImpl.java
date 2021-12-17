package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.impl.model.TestFakeReputationData;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigInteger;
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

// Разобрался как правильно заполнять таблицу reputation, могу считать сумму по столбцу count
        // для конкретного author. Но делать это получается только в NativeQuery если заводить
        //значение author через setParameter. в обычной createQery setParameter у меня не работает.
        // Так же сумма выходить в Object, а мне нужен Long как преобразовать не разобрался еще.
        //Ниже пример подсчета суммы count который работает.
        Object count = entityManager.createNativeQuery("select SUM(count) from reputation where author_id=:author ")
                .setParameter("author", id)
                .getSingleResult();
        System.out.println("Count " + count);


        questionDto.setAuthorReputation(new Long(ID)); // тестовые данные в таблицу Reputation заводил в ручную
        questionDto.setViewCount(0); //(пока не считай это поле, как оно будет считаться решим позже, пусть пока будет 0)
        questionDto.setCountAnswer(333);  // не понимаю, где брать и как считать
        questionDto.setCountValuable(444);  // не понимаю, где брать и как считать
        return questionDto;
    }
}
