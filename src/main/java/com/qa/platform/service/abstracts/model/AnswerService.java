package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerService extends ReadWriteService<Answer, Long> {

    Optional<Answer> getAnswerWithAuthor(Long answerId);
}
