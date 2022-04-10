package com.qa.platform.service.impl.dto;



import com.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.qa.platform.models.dto.PageDTO;
import com.qa.platform.models.dto.QuestionDto;
import com.qa.platform.models.dto.QuestionViewDto;
import com.qa.platform.models.dto.question.QuestionCommentDto;
import com.qa.platform.models.entity.pagination.PaginationData;
import com.qa.platform.models.entity.user.User;
import com.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends DtoServiceImpl<QuestionViewDto> implements QuestionDtoService {
    public final QuestionDtoDao questionDtoDao;
    public final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao,
                                  Map<String, PageDtoDao<QuestionViewDto>> daoMap) {
        super(daoMap);
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }


    @Override
    public List<QuestionCommentDto> getQuestionByIdComment(Long id) {
        return questionDtoDao.getQuestionIdComment(id);
    }

    @Override
    public Optional<QuestionDto> getQuestionDtoServiceById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();
        Optional<QuestionDto> q = questionDtoDao.getQuestionDtoDaoById(id, userId);
        if(q.isPresent()) {
            q.get().setListTagDto(tagDtoDao.getTagDtoDaoById(id));
            q.get().setListQuestionCommentDto(questionDtoDao.getQuestionIdComment(id));
            return q;
        }
            return Optional.empty();
    }

    @Override
    public PageDTO<QuestionViewDto> getPageDto(PaginationData properties) {
        var pageDto = super.getPageDto(properties);
        var map = tagDtoDao.getTagDtoByQuestionIds(
                pageDto.getItems().stream().map(QuestionViewDto::getId).collect(Collectors.toList())
        );
        pageDto.getItems().forEach(q -> q.setListTagDto(map.get(q.getId())));
        return pageDto;
    }
}
