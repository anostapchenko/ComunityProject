package com.javamentor.qa.platform.service.impl;

import com.javamentor.qa.platform.service.abstracts.model.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getByEmail(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Username with email '%s' not found!", username)));
    }
}
