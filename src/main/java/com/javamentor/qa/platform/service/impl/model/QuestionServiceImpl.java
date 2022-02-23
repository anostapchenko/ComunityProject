package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final QuestionDao questionDao;
    private final TagDao tagDao;

    public QuestionServiceImpl(QuestionDao questionDao, TagDao tagDao) {
        super(questionDao);
        this.questionDao = questionDao;
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Long> getCountByQuestion() {
        return questionDao.getCountQuestion();
    }

    public Optional<Question> getQuestionByIdWithAuthor(Long id){
        return questionDao.getQuestionByIdWithAuthor(id);
    }

    @Override
    public void persist(Question question) {

        List<Tag> listTagForQuestion = new ArrayList<>();

        List<String> listTagName = question.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        List<Tag> tagsThatExistsInDatabase = tagDao.getListTagsByListOfTagName(listTagName);
        Map<String, Tag> mapTagsThatExistsInDatabase = tagsThatExistsInDatabase.stream()
                .collect(Collectors.toMap(Tag::getName, tag -> tag));

        for (String tagName : listTagName) {
            if (mapTagsThatExistsInDatabase.containsKey(tagName)) {
                listTagForQuestion.add(mapTagsThatExistsInDatabase.get(tagName));
            } else {
                Tag tag = new Tag();
                tag.setName(tagName);
                tagDao.persist(tag);
                listTagForQuestion.add(tag);
            }
        }
        question.setTags(listTagForQuestion);
        super.persist(question);
    }
}
