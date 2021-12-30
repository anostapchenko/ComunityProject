package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserToUserDtoConverter {

    public static UserToUserDtoConverter INSTANCE = Mappers.getMapper( UserToUserDtoConverter.class );

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.imageLink", target = "linkImage")
    @Mapping(source = "user.city", target = "city")
    public abstract UserDto userToUserDto(User user);

    @Named("reputation")
    public int setReputation(Long reputation) {
        return reputation.intValue();
    }
}
