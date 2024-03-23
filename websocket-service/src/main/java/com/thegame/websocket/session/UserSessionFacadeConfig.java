package com.thegame.websocket.session;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class UserSessionFacadeConfig {


    private final UserSessionRepository userSessionRepository;

    @Bean
    public UserSessionFacade userSessionFacade(){
        UserSessionService userSessionService = new UserSessionServiceImpl(userSessionRepository);
        return new UserSessionFacade(userSessionService);
    }
}
