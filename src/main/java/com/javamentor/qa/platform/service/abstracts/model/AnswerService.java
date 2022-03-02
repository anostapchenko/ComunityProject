package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerService extends ReadWriteService<Answer, Long> {

    Optional<Answer> getAnswerWithAuthor(Long answerId);

    boolean existsByAnswerIdAndUserIdAndQuestionId (Long answerId, Long userId, Long questionId);
}
