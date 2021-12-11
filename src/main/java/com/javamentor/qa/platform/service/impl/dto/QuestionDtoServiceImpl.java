package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.impl.dto.QuestionDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {
    public final QuestionDtoDaoImpl questionDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDaoImpl questionDtoDao) {
        this.questionDtoDao = questionDtoDao;
    }
    public QuestionDto getQuestionDtoServiceById(Long id){
        return questionDtoDao.getQuestionDtoDaoById(id);
    }
}
