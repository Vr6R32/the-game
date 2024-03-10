package com.thegame.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserFacadeConfiguration {

    @Bean
    public UserFacade userFacade() {
        return new UserFacade();
    }

}
