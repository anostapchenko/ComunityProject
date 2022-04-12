package com.qa.platform.webapp.converters;

import com.qa.platform.models.dto.TagDto;
import com.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TagConverter {

    public abstract TagDto tagToTagDto(Tag tag);

    public abstract Tag tagDtoToTag(TagDto tagDto);

    public abstract List<Tag> listTagDtoToListTag(List<TagDto> listTagDto);
}
