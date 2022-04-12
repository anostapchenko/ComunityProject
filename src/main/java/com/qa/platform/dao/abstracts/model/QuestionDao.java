package com.qa.platform.dao.abstracts.model;

import com.qa.platform.models.entity.question.Question;

import java.util.Optional;

public interface QuestionDao extends ReadWriteDao<Question, Long> {
    Optional<Long> getCountQuestion();

    Optional<Question> getQuestionByIdWithAuthor (Long id);

}
