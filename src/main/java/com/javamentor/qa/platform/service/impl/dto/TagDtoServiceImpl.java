package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.impl.dto.QuestionDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.stereotype.Service;

@Service
public class TagDtoServiceImpl implements TagDtoService {
    public final QuestionDtoDao questionDtoDao;

    public TagDtoServiceImpl(QuestionDtoDao questionDtoDao) {
        this.questionDtoDao = questionDtoDao;
    }

    public QuestionDto getQuestionDtoServiceById(Long id){
        return questionDtoDao.getQuestionDtoDaoById(id);
    }
}
