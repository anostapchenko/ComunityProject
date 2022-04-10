package com.qa.platform.service.impl.dto;

import com.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.models.dto.AnswerDTO;
import com.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnswerDtoServiceImpl extends DtoServiceImpl<AnswerDTO> implements AnswerDtoService {

    private AnswerDtoDao answerDtoDao;

    @Autowired
    public AnswerDtoServiceImpl(Map<String, PageDtoDao<AnswerDTO>> daoMap, AnswerDtoDao answerDtoDao) {
        super(daoMap);
        this.answerDtoDao = answerDtoDao;
    }

    @Override
    public Optional<AnswerDTO> getAnswerDtoById(Long id) {
        return answerDtoDao.getAnswerDtoById(id);
    }

    @Override
    public List<AnswerDTO> getAllAnswerDtoByQuestionId(Long questionId) {
        return answerDtoDao.getAllAnswerDtoByQuestionId(questionId);
    }
}
