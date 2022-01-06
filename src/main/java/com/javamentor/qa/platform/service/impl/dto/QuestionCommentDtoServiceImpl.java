package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionCommentDtoDao;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionCommentDtoService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionCommentDtoServiceImpl implements QuestionCommentDtoService {
    private final QuestionCommentDtoDao questionCommentDtoDao;

    public QuestionCommentDtoServiceImpl(QuestionCommentDtoDao questionCommentDtoDao) {
        this.questionCommentDtoDao = questionCommentDtoDao;
    }

    @Override
    public List<QuestionCommentDto> getQuestionByIdComment(Long id) {
        return questionCommentDtoDao.getQuestionIdComment(id);
    }
}
