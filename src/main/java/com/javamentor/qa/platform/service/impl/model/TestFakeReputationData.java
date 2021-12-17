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
                "        VALUES (1, 100, null, 1, 5, 2, null, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (2, 200, null, 1, null, 2, 5, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (3, 300, null, 1, 5, 3, null, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (4, 400, null, 1, null, 3, 5, 4)").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO public.reputation (id, count, persist_date, type, answer_id, author_id, question_id, sender_id)\n" +
                "        VALUES (5, 500, null, 1, 5, 3, null, 4)").executeUpdate();
    }
}
