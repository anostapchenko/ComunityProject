package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.stereotype.Service;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
@EnableTransactionManagement
public class SetVoteReputationOfQuestionService {

    final
    UserService userService;
    QuestionService questionService;
    VoteQuestionService voteQuestionService;
    ReputationService reputationService;

    public SetVoteReputationOfQuestionService(UserService userService, QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService) {
        this.userService = userService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean validateUserVote(Long id, Long userId){
        Query queryValidateUserVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.user.id in :userId) and (v.question.id in : id )  ", VoteQuestion.class);
        queryValidateUserVote.setParameter("userId",userId);
        queryValidateUserVote.setParameter("id",id);
        return queryValidateUserVote.getResultList().size() == 0;
    }
    @Transactional
    public boolean validateQuestion(Long id){
        Query queryValidateQuestion = entityManager.createQuery("select v from Question v where v.id in : id", Question.class);
        queryValidateQuestion.setParameter("id",id);
        return queryValidateQuestion.getResultList().size() == 0;
    }

    public void setVote(long userId, long questionId, VoteType vote){
        User user = userService.getById(userId).orElse(new User());
        Question question = questionService.getById(questionId).orElse(new Question());
        VoteQuestion voteQuestion = new VoteQuestion(user,question,vote);
        voteQuestionService.persistAll(voteQuestion);
    }

    public void setReputation (long userId, long questionId, int count){
        User sender = userService.getById(userId).orElse(new User());
        Question question = questionService.getById(questionId).orElse(new Question());
        User authorQuestion = question.getUser();
        Reputation reputation = new Reputation(authorQuestion,sender,count, ReputationType.VoteQuestion,question);
        reputationService.persistAll(reputation);
    }

    @Transactional
    public int getVote(Long id){
        Query queryDownVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.question.id in :id) and (v.vote = 'DOWN_VOTE')  ", VoteQuestion.class);
        queryDownVote.setParameter("id",id);
        int downVote = queryDownVote.getResultList().size() * -1;
        Query queryUpVote = entityManager.createQuery("select v from VoteQuestion v join fetch v.question join fetch v.user where (v.question.id in :id) and (v.vote = 'UP_VOTE')  ", VoteQuestion.class);
        queryUpVote.setParameter("id",id);
        int upVote = queryUpVote.getResultList().size();
        return downVote+upVote;
    }
}
