package com.qa.platform.service.impl;

import com.qa.platform.models.entity.question.IgnoredTag;
import com.qa.platform.models.entity.question.Question;
import com.qa.platform.models.entity.question.Tag;
import com.qa.platform.models.entity.question.VoteQuestion;
import com.qa.platform.models.entity.question.TrackedTag;
import com.qa.platform.models.entity.question.answer.Answer;
import com.qa.platform.models.entity.question.answer.VoteAnswer;
import com.qa.platform.models.entity.question.answer.VoteType;
import com.qa.platform.models.entity.user.Role;
import com.qa.platform.models.entity.user.User;
import com.qa.platform.models.entity.user.reputation.Reputation;
import com.qa.platform.models.entity.user.reputation.ReputationType;
import com.qa.platform.service.abstracts.model.*;
import com.qa.platform.service.abstracts.model.AnswerService;
import com.qa.platform.service.abstracts.model.IgnoredTagService;
import com.qa.platform.service.abstracts.model.QuestionService;
import com.qa.platform.service.abstracts.model.ReputationService;
import com.qa.platform.service.abstracts.model.RoleService;
import com.qa.platform.service.abstracts.model.TagService;
import com.qa.platform.service.abstracts.model.TrackedTagService;
import com.qa.platform.service.abstracts.model.UserService;
import com.qa.platform.service.abstracts.model.VoteAnswerService;
import com.qa.platform.service.abstracts.model.VoteQuestionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TestDataInitService {

    private final RoleService roleService;
    private final UserService userService;
    private final TagService tagService;
    private final TrackedTagService trackedTagService;
    private final IgnoredTagService ignoredTagService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ReputationService reputationService;
    private final VoteQuestionService voteQuestionService;
    private final VoteAnswerService voteAnswerService;

    private final long NUM_OF_USERS = 10L;
    private final long NUM_OF_TAGS = 5L;
    private final long NUM_OF_QUESTIONS = 10L;
    private final long NUM_OF_ANSWERS = 50L;
    private final int MAX_TRACKED_TAGS = 3;
    private final int MAX_IGNORED_TAGS = 3;
    private final long NUM_OF_REPUTATIONS = 10L;
    private final long NUM_OF_VOTEQUESTIONS = 10L;
    private final long NUM_OF_VOTEANSWERS = 10L;

    public void init() {
        createRoles();
        createUsers();
        createTags();
        createTrackedAndIgnoredTags();
        createQuestions();
        createAnswers();
        createReputations();
        createVoteQuestion();
        createVoteAnswer();
    }

    public void createRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_ADMIN"));
        roles.add(new Role("ROLE_USER"));
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
                    .isEnabled(true)
                    .isDeleted(false)
                    .imageLink("/images/noUserAvatar.png")
                    .build();
            users.add(user);
        }

        userService.persistAll(users);
    }

    public void createTags() {
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_TAGS; i++) {
            String generatedString = RandomStringUtils.randomAlphabetic(6);
            Tag tag = Tag.builder()
                    .name(generatedString + i)
                    .description("Description of tag " + i)
                    .persistDateTime(LocalDateTime.now())
                    .build();
            tags.add(tag);
        }

        tagService.persistAll(tags);
    }

    public void createTrackedAndIgnoredTags() {
        List<TrackedTag> trackedTags = new ArrayList<>();
        List<IgnoredTag> ignoredTags = new ArrayList<>();
        List<Tag> tags = tagService.getAll();
        List<User> users = userService.getAll();
        users.remove(0);

        for (User user : users) {
            Collections.shuffle(tags);
            int numOfTrackedTags = new Random().nextInt(MAX_TRACKED_TAGS);
            int numOfIgnoredTags = new Random().nextInt(MAX_IGNORED_TAGS);
            int numOfTags = Math.min((numOfTrackedTags + numOfIgnoredTags), tags.size());
            for (int i = 0; i < numOfTags; i++) {
                if (i < numOfTrackedTags) {
                    TrackedTag trackedTag = TrackedTag.builder()
                            .user(user)
                            .trackedTag(tags.get(i))
                            .build();
                    trackedTags.add(trackedTag);
                } else {
                    IgnoredTag ignoredTag = IgnoredTag.builder()
                            .user(user)
                            .ignoredTag(tags.get(i))
                            .build();
                    ignoredTags.add(ignoredTag);
                }
            }
        }

        trackedTagService.persistAll(trackedTags);
        ignoredTagService.persistAll(ignoredTags);
    }

    public void createQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= NUM_OF_QUESTIONS; i++) {
            Question question = Question.builder()
                    .title("Question " + i)
                    .description("What you think about question " + i + "?")
                    .persistDateTime(LocalDateTime.now().minusDays(i))
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
    public void createReputations() {
        List<Reputation> reputations = new ArrayList<>();
        for (long i = 1; i <= NUM_OF_REPUTATIONS; i++) {
            Reputation reputation = Reputation.builder()
                    .persistDate(LocalDateTime.now())
                    .author(userService.getById(i).get()) // При getRandomUser  могут не быть все авторы
                    .sender(null)
                    .count(((Number) (getRandomUser().getId() * 100)).intValue())
                    .type(ReputationType.Question)
                    .question(getRandomQuestion())
                    .answer(null)
                    .build();
            reputations.add(reputation);
        }
        reputationService.persistAll(reputations);
    }
    public void createVoteQuestion() {
        List<VoteQuestion> voteQuestions = new ArrayList<>();
        for (long i = 1; i <= NUM_OF_VOTEQUESTIONS; i++) {
            VoteQuestion voteQuestion = VoteQuestion.builder()
                    .question(questionService.getById(i).get()) // При getRandomQuestion могут не быть все вопросы
                    .vote(new Random().nextInt(100) % 2 == 0 ? VoteType.UP_VOTE : VoteType.DOWN_VOTE)
                    .localDateTime(LocalDateTime.now())
                    .user(getRandomUser())
                    .build();
            voteQuestions.add(voteQuestion);
        }
        voteQuestionService.persistAll(voteQuestions);
    }

    public void createVoteAnswer() {
        List<VoteAnswer> voteAnswers = new ArrayList<>();
        for (long i = 1; i <= NUM_OF_VOTEANSWERS; i++) {
            VoteAnswer voteAnswer = VoteAnswer.builder()
                    .answer(answerService.getById(i).get())
                    .vote(new Random().nextInt(100) % 2 == 0 ? VoteType.UP_VOTE : VoteType.DOWN_VOTE)
                    .persistDateTime(LocalDateTime.now())
                    .user(getRandomUser())
                    .build();
            voteAnswers.add(voteAnswer);
        }
        voteAnswerService.persistAll(voteAnswers);
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


}
