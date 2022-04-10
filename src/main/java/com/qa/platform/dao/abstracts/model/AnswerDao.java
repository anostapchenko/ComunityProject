package com.qa.platform.dao.abstracts.model;

import com.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {

    Optional<Answer> getAnswerWithAuthor(long answerId);
}
