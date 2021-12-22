package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.stereotype.Service;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private final VoteAnswerDao voteAnswerDao;


    public VoteAnswerServiceImpl(VoteAnswerDao voteAnswerDao) {
        super(voteAnswerDao);
        this.voteAnswerDao = voteAnswerDao;
    }
}
