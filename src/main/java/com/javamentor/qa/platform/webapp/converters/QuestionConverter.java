package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Mapper(componentModel = "spring")
public abstract class QuestionConverter {


    @Mapping(source = "tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.nickname", target = "authorName")
    @Mapping(source = "question.user.imageLink", target = "authorImage")
    @Mapping(constant = "0", target = "countAnswer")
    @Mapping(constant = "0", target = "countValuable")
    @Mapping(constant = "0", target = "viewCount")
    public abstract QuestionDto questionToQuestionDto(Question question);


//    public QuestionConverter INSTANCE = Mappers.getMapper(QuestionConverter.class );

    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
//    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "listTagDto", target = "tags")
    public abstract Question questionDtoToQuestion(QuestionDto questionDto);


//    @Named("userIdToSet")
//    public Set<User> userIdToSet(Long userId) {
//        User user = userService.getById(userId);
//        Set<User> userSet = new HashSet<>();
//        userSet.add(user);
//        return userSet;
//    }

}
