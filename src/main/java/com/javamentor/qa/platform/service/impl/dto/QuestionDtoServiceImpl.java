package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.exception.NoSuchDaoException;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionDtoServiceImpl extends DtoServiceImpl<QuestionDto> implements QuestionDtoService {
    public final QuestionDtoDao questionDtoDao;
    public final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao, Map<String, PageDtoDao<QuestionDto>> daoMap) {
        super(daoMap);
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

    @Override
    public PageDTO<QuestionDto> getPageDto(PaginationData properties) {
        PageDTO<QuestionDto> pageDTO = super.getPageDto(properties);
        List<Long> questionIds = new ArrayList<>();
        List<QuestionDto> questionDtoList = pageDTO.getItems();

        for(QuestionDto questionDto: questionDtoList) {
            questionIds.add(questionDto.getId());
        }

        Map<Long, List<TagDto>> tagDtoMap = tagDtoDao.getTagDtoByQuestionsId(questionIds);

        for(QuestionDto questionDto: questionDtoList) {
            if(tagDtoMap.containsKey(questionDto.getId())) {
                questionDto.setListTagDto(tagDtoMap.get(questionDto.getId()));
            }
        }
        return pageDTO;
    }


}
