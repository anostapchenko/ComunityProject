package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;

import java.util.List;

import java.util.Optional;

public interface QuestionService extends ReadWriteService<Question, Long> {

    Optional<Question> getQuestionByIdWithAuthor (Long id);
}
