package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TestDataInitService {

    private final RoleService roleService;
    private final UserService userService;
    private final TagService tagService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    private final long NUM_OF_ROLES = 3L;
    private final long NUM_OF_USERS = 10L;
    private final long NUM_OF_TAGS = 5L;
    private final long NUM_OF_QUESTIONS = 10L;
    private final long NUM_OF_ANSWERS = 50L;

    public TestDataInitService(RoleService roleService, UserService userService, TagService tagService, QuestionService questionService, AnswerService answerService) {
        this.roleService = roleService;
        this.userService = userService;
        this.tagService = tagService;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    public void createRoles() {
        List<Role> roles = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_ROLES; i++) {
            Role role = Role.builder()
                    .name("ROLE_ROLE" + i)
                    .build();
            roles.add(role);
        }

        roleService.persistAll(roles);
    }

    public void createUsers() {
        List<User> users = new ArrayList<>();
        List<Role> roles = roleService.getAll();
        for (int i = 1; i <= NUM_OF_USERS; i++) {
            Role role = roles.get(new Random().nextInt(roles.size()));
            User user = User.builder()
                    .email("user" + i + "@mail.ru")
                    .password("user" + i)
                    .fullName("User " + i)
                    .city("Moscow")
                    .about("I'm Test user #" + i)
                    .nickname("user_" + i)
                    .role(role)
                    .build();
            users.add(user);
        }

        userService.persistAll(users);
    }

    public void createTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_TAGS; i++) {
            Tag tag = Tag.builder()
                    .name("Tag " + i)
                    .description("Description of tag " + i)
                    .build();
            tags.add(tag);
        }

        tagService.persistAll(tags);
    }

    public void createQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_QUESTIONS; i++) {
            Question question = Question.builder()
                    .title("Question " + i)
                    .description("What you think about question " + i + "?")
                    .user(getRandomUser())
                    .tags(getRandomTagList())
                    .build();
            questions.add(question);
        }

        questionService.persistAll(questions);
    }

    public void createAnswers() {
        List<Answer> answers = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_ANSWERS; i++) {
            Answer answer = Answer.builder()
                    .htmlBody("Answer " + i)
                    .user(getRandomUser())
                    .question(getRandomQuestion())
                    .isDeleted(false)
                    .isHelpful(false)
                    .isDeletedByModerator(false)
                    .build();
            answers.add(answer);
        }

        answerService.persistAll(answers);
    }

    private List<Tag> getRandomTagList() {
        List<Tag> tags = tagService.getAll();
        int numOfDeleteTags = new Random().nextInt(tags.size());
        for (int i = 0; i < numOfDeleteTags; i++) {
            tags.remove(new Random().nextInt(tags.size()));
        }
        return tags;
    }

    private User getRandomUser() {
        List<User> users = userService.getAll();
        return users.get(new Random().nextInt(users.size()));
    }

    private Question getRandomQuestion() {
        List<Question> questions = questionService.getAll();
        return questions.get(new Random().nextInt(questions.size()));
    }

    public void init() {
        createRoles();
        createUsers();
        createTags();
        createQuestions();
        createAnswers();
    }

}
