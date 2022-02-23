package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.stereotype.Service;

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

    }
