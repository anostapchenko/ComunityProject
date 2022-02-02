package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {

    private final QuestionService questionService;
    private final QuestionViewedDao questionViewedDao;

    public QuestionViewedServiceImpl(QuestionService questionService, QuestionViewedDao questionViewedDao) {
        super(questionViewedDao);
        this.questionService = questionService;
        this.questionViewedDao = questionViewedDao;
    }

    @Override
    public List<QuestionViewed> getQuestionViewedByUserAndQuestion(Long userId, Long questionId){
        return questionViewedDao.getQuestionViewedByUserAndQuestion(userId, questionId);
    }

    @Override
    @Cacheable("QuestionViewed")
    public void markQuestionLikeViewed(User user, Long questionId) {

        Optional<Question> question = questionService.getById(questionId);
        if (question.isPresent()){
            if(getQuestionViewedByUserAndQuestion(user.getId(), questionId).isEmpty()){
                persist(QuestionViewed.builder()
                        .question(question.get())
                        .user(user)
                        .build());
            }
        }
    }
}
