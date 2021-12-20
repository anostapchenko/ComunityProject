package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {

    private final ReputationDao reputationDao;
    private final UserDao userDao;
    private final QuestionDao questionDao;

    public ReputationServiceImpl(ReputationDao reputationDao, UserDao userDao, QuestionDao questionDao){
        super(reputationDao);
        this.reputationDao = reputationDao;
        this.userDao = userDao;
        this.questionDao = questionDao;
    }

    @Override
    @Transactional
    public void setReputation (long userId, long questionId, int count){
        User sender = userDao.getById(userId).orElse(new User());
        Question question = questionDao.getById(questionId).orElse(new Question());
        User authorQuestion = question.getUser();
        Reputation reputation = new Reputation(authorQuestion,sender,count, ReputationType.VoteQuestion,question);
        reputationDao.persistAll(reputation);
    }

}
