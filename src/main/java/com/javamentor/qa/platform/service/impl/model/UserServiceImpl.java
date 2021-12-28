package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl<User, Long> implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        super(userDao);
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void persistAll(User... users) {
        Arrays.stream(users).forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
        super.persistAll(users);
    }

    @Override
    public void persistAll(Collection<User> users) {
        users.forEach(user -> user.setPassword(passwordEncoder.encode(user.getPassword())));
        super.persistAll(users);
    }

    @Override
    public void updateAll(Iterable<? extends User> users) {
        users.forEach(user -> {
            String oldPassword = getById(user.getId()).get().getPassword();
            if (user.getPassword().trim().isEmpty()) { user.setPassword(oldPassword); }
            else if (!user.getPassword().equals(oldPassword)) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        });
        super.updateAll(users);
    }

    @Override
    public void persist(User user) {
        user.setId(null); // Just in case :)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        super.persist(user);
    }

    @Override
    public void update(User user) {
        // Check password for changes...
        // This method don't allow empty passwords!
        String oldPassword = getById(user.getId()).get().getPassword();
        if (user.getPassword().trim().isEmpty()) { user.setPassword(oldPassword); }
        else if (!user.getPassword().equals(oldPassword)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        super.update(user);
    }

    @Override
    @Transactional
    public Optional<User> getByEmail(String email) {
        return userDao.getWithRoleByEmail(email);
    }

    @Override
    public boolean disableUserWithEmail(String email) {
        userDao.disableUserWithEmail(email);
        return false;
    }
}
