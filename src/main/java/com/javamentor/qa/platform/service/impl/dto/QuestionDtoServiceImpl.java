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
        Optional<QuestionDto> q = questionDtoDao.getQuestionDtoDaoById(id);
        if(q.isPresent()){
            q.get().setListTagDto(tagDtoDao.getTagDtoDaoById(id));
            return q;
        }
            return Optional.empty();
    }
}
