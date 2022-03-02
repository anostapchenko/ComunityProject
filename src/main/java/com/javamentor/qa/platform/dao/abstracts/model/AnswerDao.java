package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {

    Optional<Answer> getAnswerWithAuthor(long answerId);

    boolean existsByAnswerIdAndUserIdAndQuestionId (Long answerId, Long userId, Long questionId);
}
