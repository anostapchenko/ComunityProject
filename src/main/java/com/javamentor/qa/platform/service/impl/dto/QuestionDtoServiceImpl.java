package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {
    public final QuestionDtoDao questionDtoDao;
    public final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao) {
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public Optional<QuestionDto> getQuestionDtoServiceById(Long id){
        if(questionDtoDao.getQuestionDtoDaoById(id).isPresent()){
            QuestionDto questionDto = questionDtoDao.getQuestionDtoDaoById(id).get();
            questionDto.setListTagDto(tagDtoDao.getTagDtoDaoById(id));
            return Optional.of(questionDto);
        } else {
            return Optional.empty();
        }
    }
}
