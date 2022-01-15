package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.util.StringResponse;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {

    Optional<User> getByEmail(String email);

    StringResponse changePassword(String password, Authentication authentication);

}
