package com.thegame.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryDAO userRepositoryDAO;

    public UserDetailsServiceImpl(JdbcTemplate jdbcTemplate) {
        userRepositoryDAO = new UserRepositoryDAO(jdbcTemplate);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryDAO.findByUsername(username);
    }

}