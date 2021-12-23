package com.javamentor.qa.platform.service.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.service.abstracts.pagination.AnswerPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnswerDtoServiceImpl extends DtoServiceImpl<AnswerDTO> implements AnswerPageDtoService {
    @Autowired
    public AnswerDtoServiceImpl(Map<String, PageDtoDao<AnswerDTO>> daoMap) {
        super(daoMap);
    }
}
