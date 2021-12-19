package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableTransactionManagement

public class TestFakeReputationData {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserService userService;

    public TestFakeReputationData(UserService userService) {
        this.userService = userService;
    }

        @Transactional
    public void putFakeReputationData(){

            for (Long i = 1L; i <= 10L; i++) {
                Reputation rep = Reputation.builder()
                        .persistDate(LocalDateTime.now())
                        .author(entityManager.find(User.class, i))
                        .sender(null)
                        .count(((Number) (i * 100)).intValue())
                        .type(ReputationType.Question)
                        .question(entityManager.find(Question.class, i))
                        .answer(null)
                        .build();
                entityManager.persist(rep);
            }
    }
}
