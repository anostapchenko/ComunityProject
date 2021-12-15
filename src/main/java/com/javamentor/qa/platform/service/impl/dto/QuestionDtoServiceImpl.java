package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.impl.dto.QuestionDtoDaoImpl;
import com.javamentor.qa.platform.dao.impl.dto.TagDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {
    public final QuestionDtoDaoImpl questionDtoDao;
    public final TagDtoDaoImpl tagDtoDao;


    public QuestionDtoServiceImpl(QuestionDtoDaoImpl questionDtoDao, TagDtoDaoImpl tagDtoDao) {
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }
    public QuestionDto getQuestionDtoServiceById(Long id){
        QuestionDto q = questionDtoDao.getQuestionDtoDaoById(id);
        q.setListTagDto(tagDtoDao.getTagDtoDaoById(id));
        return q;
    }
}
