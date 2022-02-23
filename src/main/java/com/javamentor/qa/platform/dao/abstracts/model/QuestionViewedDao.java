package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.QuestionViewed;

import java.util.List;

public interface QuestionViewedDao extends ReadWriteDao<QuestionViewed, Long>{

    List<QuestionViewed> getQuestionViewedByUserAndQuestion (String email, Long questionId);

}
