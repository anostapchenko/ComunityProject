package com.qa.platform.service.impl.model;

import com.qa.platform.dao.abstracts.model.ReputationDao;
import com.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.question.VoteQuestion;
import com.qa.platform.models.entity.user.User;
import com.qa.platform.models.entity.user.reputation.Reputation;
import com.qa.platform.models.entity.user.reputation.ReputationType;
import com.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion,Long> implements VoteQuestionService {

    private  final VoteQuestionDao voteQuestionDao;
    private  final ReputationDao reputationDao;

    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao, ReputationDao reputationDao  ){
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
        this.reputationDao = reputationDao;
    }
    @Override
    @Transactional
    public void persist(VoteQuestion voteQuestion){
        Question question = voteQuestion.getQuestion();
        User user = voteQuestion.getUser();
        User authorQuestion = question.getUser();
        int countVq = voteQuestion.getCountVQ();
        Reputation reputation = new Reputation(authorQuestion,user,countVq, ReputationType.VoteQuestion,question);
        voteQuestionDao.persist(voteQuestion);
        reputationDao.persist(reputation);
    }

    @Override
    public boolean validateUserVoteByQuestionIdAndUserId(Long questionId, Long userId) {
        return voteQuestionDao.isUserNotVoteByQuestionIdAndUserId(questionId, userId);
    }

    @Override
    public Long getVoteByQuestionId(Long questionId) {
        return voteQuestionDao.getVoteByQuestionId(questionId);
    }


}
