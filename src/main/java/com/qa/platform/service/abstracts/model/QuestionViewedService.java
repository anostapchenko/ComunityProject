package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.question.QuestionViewed;
import com.qa.platform.models.entity.user.User;

public interface QuestionViewedService extends ReadWriteService<QuestionViewed, Long>  {

    void markQuestionLikeViewed(User user, Question question);

}
