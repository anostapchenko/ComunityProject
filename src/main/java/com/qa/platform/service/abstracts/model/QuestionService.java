package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.question.Question;

import java.util.Optional;

public interface QuestionService extends ReadWriteService<Question, Long> {
    Optional<Long> getCountByQuestion();

    Optional<Question> getQuestionByIdWithAuthor(Long id);

}
