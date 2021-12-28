package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
<<<<<<< HEAD
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
=======
import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> dev
import org.springframework.stereotype.Service;

import java.util.List;

@Service
<<<<<<< HEAD
public class TagDtoServiceImpl extends ReadWriteServiceImpl<TagDto, Long> implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        super(tagDtoDao);
        this.tagDtoDao = tagDtoDao;
    }

    public List<TagDto> getIgnoredTagByUserId(Long userId){
        return tagDtoDao.getIgnoredTagByUserId(userId);
    }
}

=======
public class TagDtoServiceImpl implements TagDtoService {

    private TagDtoDao tagDtoDao;

    @Autowired
    public void setTagDtoDao(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getTrackedTagsByUserId(Long currentUserId) {
        return tagDtoDao.getTrackedTagsByUserId(currentUserId);
    }
}
>>>>>>> dev
