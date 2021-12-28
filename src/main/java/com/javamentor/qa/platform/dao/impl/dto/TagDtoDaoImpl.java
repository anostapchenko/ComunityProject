package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.impl.model.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDtoDaoImpl extends ReadWriteDaoImpl<TagDto, Long> implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getIgnoredTagByUserId(Long userId){
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TagDto(tag.id, tag.name) " +
                   "from IgnoredTag ignTag inner join ignTag.user  " +
                   "left join ignTag.ignoredTag tag where ignTag.user.id=:userId",
                    TagDto.class)
                .setParameter("userId",userId)
                .getResultList();
    }

}
