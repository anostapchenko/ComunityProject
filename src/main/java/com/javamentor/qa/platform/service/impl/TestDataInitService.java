package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TestDataInitService {

    private final RoleService roleService;
    private final UserService userService;

    private final long NUM_OF_ROLES = 3L;
    private final long NUM_OF_USERS = 10L;

    public TestDataInitService(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
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

    public void init() {
        createRoles();
        createUsers();
    }

}
