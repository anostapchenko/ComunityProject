package com.javamentor.qa.platform.webapp.configs.initializer;

import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.service.impl.model.TestFakeReputationData;
import com.javamentor.qa.platform.service.impl.model.TestFakeVoteQuestionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingClass({"org.springframework.boot.test.context.SpringBootTest"})
public class TestEntityInit implements CommandLineRunner {

    private final TestDataInitService testDataInitService;
    private final TestFakeReputationData testFakeReputationData;
    private final TestFakeVoteQuestionData testFakeVoteQuestionData;

    @Autowired
    public TestEntityInit(TestDataInitService testDataInitService, TestFakeReputationData testFakeReputationData, TestFakeVoteQuestionData testFakeVoteQuestionData) {
        this.testDataInitService = testDataInitService;
        this.testFakeReputationData = testFakeReputationData;
        this.testFakeVoteQuestionData = testFakeVoteQuestionData;
    }

    @Override
    public void run(String... args) {
        testDataInitService.init();
        testFakeReputationData.putFakeReputationData();
        testFakeVoteQuestionData.putFakeVoteQuestionData();
    }
}
