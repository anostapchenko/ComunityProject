package com.qa.platform.service.impl.model;

import com.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.question.QuestionViewed;
import com.qa.platform.models.entity.user.User;
import com.qa.platform.service.abstracts.model.QuestionService;
import com.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void markQuestionLikeViewed(User user, Question question) {

        if(questionViewedDao.getQuestionViewedByUserAndQuestion(user.getEmail(), question.getId()).isEmpty()){
            persist(QuestionViewed.builder()
                    .question(question)
                    .user(user)
                    .build());
        }
    }
}
