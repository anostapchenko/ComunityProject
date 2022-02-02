package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionViewedDao extends ReadWriteDao<QuestionViewed, Long>{

    List<QuestionViewed> getQuestionViewedByUserAndQuestion (Long userId, Long questionId);

}
