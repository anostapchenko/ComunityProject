package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDto;
import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.dto.TagDTO;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class TagDtoImpl extends ReadWriteDaoImpl<TagDto, Long> implements TagDto {

    @PersistenceContext
    private EntityManager entityManager;

//    List<TagDto> tagDtos = entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.TagDTO(t.id, t.name) from Tag t", CityDto.class)
//            .getResultList();
}
