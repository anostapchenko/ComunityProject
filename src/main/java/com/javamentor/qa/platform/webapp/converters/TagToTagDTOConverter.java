package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class TagToTagDTOConverter {

//    public TagToTagDTOConverter INSTANCE = Mappers.getMapper( TagToTagDTOConverter.class );

    @Mapping(source = "tag.id", target = "id")
    @Mapping(source = "tag.name", target = "name")
    @Mapping(source = "tag.persistDateTime", target = "persistDateTime")
    public abstract TagDto tagToTagDTONotQuestAndDescription(Tag tag);
}
