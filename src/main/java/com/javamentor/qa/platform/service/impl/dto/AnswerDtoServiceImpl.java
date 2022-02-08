package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnswerDtoServiceImpl extends DtoServiceImpl<AnswerDTO> implements AnswerDtoService {
    @Autowired
    public AnswerDtoServiceImpl(Map<String, PageDtoDao<AnswerDTO>> daoMap) {
        super(daoMap);
    }


}
