package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.stereotype.Service;

@Service
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion,Long> implements VoteQuestionService {

    private final VoteQuestionDao voteQuestionDao;

    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao){
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
    }
}
