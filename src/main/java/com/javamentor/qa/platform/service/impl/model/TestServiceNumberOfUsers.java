package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
@EnableTransactionManagement
public class TestServiceNumberOfUsers {

    final
    UserService userService;
    QuestionService questionService;
    VoteQuestionService voteQuestionService;
    ReputationService reputationService;

    @PersistenceContext
    private EntityManager entityManager;

    public TestServiceNumberOfUsers(UserService userService, QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService) {
        this.userService = userService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
    }

    @Transactional
    public int getNumberOfUsers(){
        Query query = entityManager.createQuery("select u from User u", User.class);
        return query.getResultList().size();
    }
    @Transactional
    public List<User> getAllUser2(){
        Query query = entityManager.createQuery("select u from User u join fetch u.role", User.class);
        return query.getResultList();
    }
}
