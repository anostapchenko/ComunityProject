package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;

<<<<<<<<< Temporary merge branch 1
import java.util.List;

public interface QuestionService extends ReadWriteService<Question, Long> {
=========
import java.util.Optional;

public interface QuestionService extends ReadWriteService<Question, Long> {

    Optional<Question> getQuestionByIdWithAuthor (Long id);
>>>>>>>>> Temporary merge branch 2
}
