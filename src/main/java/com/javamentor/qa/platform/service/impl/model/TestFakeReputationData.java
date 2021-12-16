package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
@EnableTransactionManagement
public class TestFakeReputationData {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void putFakeReputationData(){
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (1, 100, null, 4, 4, 1, 4, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (2, 100, null, 4, 4, 2, 4, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (3, 100, null, 4, 4, 3, 4, 4)").executeUpdate();

        // Пробовал создать объект и записать не принимает(.

//        Reputation rep = new Reputation(1L, null, null, null, 5, null, null, null);
//        entityManager.persist(rep);
//        entityManager.flush();
//        entityManager.clear();
    }
}
