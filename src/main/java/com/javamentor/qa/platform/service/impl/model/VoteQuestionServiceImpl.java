package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion,Long> implements VoteQuestionService {

    final
    VoteQuestionDao voteQuestionDao;
    UserDao userDao;
    QuestionDao questionDao;

    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao, UserDao userDao, QuestionDao questionDao  ){
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
        this.questionDao = questionDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void setVote(long userId, long questionId, VoteType vote){
        User user = userDao.getById(userId).orElse(new User());
        Question question = questionDao.getById(questionId).orElse(new Question());
        VoteQuestion voteQuestion = new VoteQuestion(user,question,vote);
        voteQuestionDao.persistAll(voteQuestion);
    }

    @Override
    public boolean validateUserVote(Long id, Long userId) {
        return voteQuestionDao.isUserVoteByQuestionIdAndUserId(id, userId);
    }

    @Override
    public int getVote(Long id) {
        return voteQuestionDao.getVote(id);
    }


}
