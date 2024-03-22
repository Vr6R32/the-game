package com.thegame.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class UserFacadeConfiguration {

    private final UserRepository userRepository;

    @Bean
    public UserFacade userFacade() {
        UserService userService = new UserServiceImpl(userRepository);
        return new UserFacade(userService);
    }

}
