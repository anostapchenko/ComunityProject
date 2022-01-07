package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Mapper(componentModel = "spring")
public abstract class QuestionConverter {
//    public QuestionConverter INSTANCE = Mappers.getMapper(QuestionConverter.class );


    @Mapping(source = "tags", target = "listTagDto")
    @Mapping(source = "question.user.id", target = "authorId")
    @Mapping(source = "question.user.nickname", target = "authorName")
    @Mapping(source = "question.user.imageLink", target = "authorImage")
    @Mapping(constant = "0", target = "countAnswer")
    @Mapping(constant = "0", target = "countValuable")
    @Mapping(constant = "0", target = "viewCount")
    public abstract QuestionDto questionToQuestionDto(Question question);



    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
//    @Mapping(source = "listTagDto", target = "tags")
    @Mapping(source = "tags", target = "tags")
    public abstract Question questionDtoToQuestion(QuestionCreateDto questionCreateDto);


//    @Named("userIdToSet")
//    public Set<User> userIdToSet(Long userId) {
//        User user = userService.getById(userId);
//        Set<User> userSet = new HashSet<>();
//        userSet.add(user);
//        return userSet;
//    }

}
