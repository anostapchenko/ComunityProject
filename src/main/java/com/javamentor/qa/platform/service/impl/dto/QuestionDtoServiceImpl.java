package com.javamentor.qa.platform.service.impl.dto;



import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.question.QuestionCommentDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends DtoServiceImpl<QuestionDto> implements QuestionDtoService {
    public final QuestionDtoDao questionDtoDao;
    public final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao,
                                  Map<String, PageDtoDao<QuestionDto>> daoMap) {
        super(daoMap);
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }


    @Override
    public List<QuestionCommentDto> getQuestionByIdComment(Long id) {
        return questionDtoDao.getQuestionIdComment(id);
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
        var pageDto = super.getPageDto(properties);
        var map = tagDtoDao.getTagDtoByQuestionIds(
                pageDto.getItems().stream().map(QuestionDto::getId).collect(Collectors.toList())
        );
        pageDto.getItems().forEach(q -> q.setListTagDto(map.get(q.getId())));
        return pageDto;
    }
}
