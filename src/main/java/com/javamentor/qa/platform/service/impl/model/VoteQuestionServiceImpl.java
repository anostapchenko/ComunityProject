package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.exception.ConstrainException;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.jca.cci.CciOperationNotSupportedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion,Long> implements VoteQuestionService {

    final
    VoteQuestionDao voteQuestionDao;
    UserDao userDao;
    QuestionDao questionDao;
    ReputationDao reputationDao;

    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao, UserDao userDao, QuestionDao questionDao, ReputationDao reputationDao  ){
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
        this.questionDao = questionDao;
        this.userDao = userDao;
        this.reputationDao = reputationDao;
    }

    @Override
    @Transactional
    public void setVoteAndSetReputation(long userId, long questionId, VoteType vote, int count){
        User user = userDao.getById(userId).orElseThrow(() -> new ConstrainException("Can't find user with id: "+ userId));
        Question question = questionDao.getById(questionId).orElseThrow(() -> new ConstrainException("Can't find question with id: "+ questionId));
        VoteQuestion voteQuestion = new VoteQuestion(user,question,vote);
        User authorQuestion = question.getUser();
        Reputation reputation = new Reputation(authorQuestion, user,count, ReputationType.VoteQuestion,question);
        voteQuestionDao.persist(voteQuestion);
        reputationDao.persist(reputation);
    }

    @Override
    public boolean validateUserVote(Long id, Long userId) {
        return voteQuestionDao.isUserVoteByQuestionIdAndUserId(id, userId);
    }

    @Override
    public Long getVote(Long id) {
        return voteQuestionDao.getVote(id);
    }


}
