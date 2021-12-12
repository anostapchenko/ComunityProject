package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements QuestionDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<TagDto> getTagDtoDaoById(Long id) {

        TypedQuery<TagDto> q = entityManager.createQuery(
                "SELECT new com.javamentor.qa.platform.models.dto.TagDto(" +
                        "t.id, t.name, t.description)" +
                        " FROM Tag t  WHERE t.id =: id ", TagDto.class);
        q.setParameter("id", id);

        List<TagDto> tagDto = q.getResultList();

        return tagDto;
    }
}
