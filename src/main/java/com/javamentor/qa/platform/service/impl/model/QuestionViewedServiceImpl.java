package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionViewedDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionViewedService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionViewedServiceImpl extends ReadWriteServiceImpl<QuestionViewed, Long> implements QuestionViewedService {

    private final QuestionService questionService;
    private final QuestionViewedDao questionViewedDao;
    private static CacheManager cacheManager;

    public QuestionViewedServiceImpl(QuestionService questionService, QuestionViewedDao questionViewedDao, CacheManager cacheManager) {
        super(questionViewedDao);
        this.questionService = questionService;
        this.questionViewedDao = questionViewedDao;
        this.cacheManager = cacheManager;
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

    @PostPersist
    @PostRemove
    void cacheHandler(QuestionViewed e) {
        cacheManager.getCache("QuestionViewed").evictIfPresent(e.getQuestion().getId()+e.getUser().getEmail());
    }
}
