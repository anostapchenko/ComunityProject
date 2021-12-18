package com.javamentor.qa.platform.controllertest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.webapp.controllers.dtocontroller.UserResourseController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@WebMvcTest(controllers = UserResourseController.class)
@DisplayName("Тестирование контроллера")
public class UserDtoControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("Принудительная валидация находит ошибку")
    void manualValidationTest() {
        UserDto emp = new UserDto(1L, "email@server.com", null, null, "Kogalym", 1);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(emp);

        ConstraintViolation<UserDto> violation = violations.stream().findFirst().orElseThrow(() -> new RuntimeException(""));

    }
}
