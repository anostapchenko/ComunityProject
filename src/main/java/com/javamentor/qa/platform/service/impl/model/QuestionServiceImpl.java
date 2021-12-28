package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.stereotype.Service;

<<<<<<<<< Temporary merge branch 1
import java.util.List;
=========
import java.util.Optional;
>>>>>>>>> Temporary merge branch 2

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        super(questionDao);
        this.questionDao = questionDao;
    }

    @Override
    public Optional<Question> getQuestionByIdWithAuthor(Long id){
        return questionDao.getQuestionByIdWithAuthor(id);
    }
}
