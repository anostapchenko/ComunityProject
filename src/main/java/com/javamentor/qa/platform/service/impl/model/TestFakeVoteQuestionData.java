package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
@EnableTransactionManagement
public class TestFakeVoteQuestionData {
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void putFakeVoteQuestionData(){
        for (Long i = 1L; i <=10 ; i++) {
            VoteQuestion vq = VoteQuestion.builder()
                    .question(entityManager.find(Question.class, i))
                    .vote(i % 2 == 0 ? VoteType.UP_VOTE : VoteType.DOWN_VOTE)
                    .localDateTime(LocalDateTime.now())
                    .user(entityManager.find(User.class, i))
                    .build();
            entityManager.persist(vq);
        }
    }
}
