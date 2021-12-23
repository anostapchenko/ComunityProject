package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDTODao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.dto.question.TagDTO;
import com.javamentor.qa.platform.service.abstracts.dto.TagDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagDTOServiceImpl implements TagDTOService {

    private TagDTODao tagDTODaoImpl;

    @Autowired
    public void setTagDTODaoImpl(TagDTODao tagDTODaoImpl) {
        this.tagDTODaoImpl = tagDTODaoImpl;
    }

    @Override
    public List<TagDTO> getTagsDTOByUserIdFromTrackedTag(Long currentUserId) {
        return tagDTODaoImpl.getTagDTOListByUserIdFromTrackedTag(currentUserId);
    }
}
