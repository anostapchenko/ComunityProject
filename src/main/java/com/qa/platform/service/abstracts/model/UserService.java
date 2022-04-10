package com.qa.platform.service.abstracts.model;

import com.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {

    Optional<User> getByEmail(String email);

    void changePassword(String password, User user);

    void deleteById(String email);

}
