package com.javamentor.qa.platform;


import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.junit5.api.DBRider;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@DBRider
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(classes = JmApplication.class)
@DBUnit(cacheConnection = false, leakHunter = true,caseSensitiveTableNames = true,allowEmptyFields = true )
public class DbRiderTest {

    @Test
    @DataSet(value = "dataset/users.yml", strategy = SeedStrategy.INSERT)
    public void setUpUsers() {
    }

}
