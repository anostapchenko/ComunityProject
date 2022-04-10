package com.qa.platform;

import com.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = JmApplication.class)
@TestPropertySource("classpath:application-test.properties")
class PlatformApplicationTests {

    @Test
    void contextLoads() {
    }

}
