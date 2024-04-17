package com.thegame.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
class UserFacadeConfiguration {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserFacade userFacade() {
        UserService userService = new UserServiceImpl(userRepository,passwordEncoder);
        return new UserFacade(userService);
    }

}
