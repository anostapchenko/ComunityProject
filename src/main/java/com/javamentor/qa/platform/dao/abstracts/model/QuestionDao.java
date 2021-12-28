package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;

<<<<<<<<< Temporary merge branch 1
import java.util.List;
=========
import java.util.Optional;
>>>>>>>>> Temporary merge branch 2

public interface QuestionDao extends ReadWriteDao<Question, Long> {

    Optional<Question> getQuestionByIdWithAuthor (Long id);

}
