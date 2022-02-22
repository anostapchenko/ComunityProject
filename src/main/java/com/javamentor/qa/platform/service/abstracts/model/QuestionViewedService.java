package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.QuestionViewed;
import com.javamentor.qa.platform.models.entity.user.User;

public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long>  {

    void markQuestionLikeViewed(User user, Question question);

}
