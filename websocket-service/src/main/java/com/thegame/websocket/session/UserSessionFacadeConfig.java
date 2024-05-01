package com.thegame.websocket.session;

import com.thegame.websocket.eureka.EurekaClientInfo;
import com.thegame.websocket.notification.NotificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class UserSessionFacadeConfig {


    private final UserSessionRepository userSessionRepository;
    private final NotificationFacade notificationFacade;
    private final EurekaClientInfo eurekaClientInfo;


    @Bean
    public UserSessionFacade userSessionFacade(){
        UserSessionService userSessionService = new UserSessionServiceImpl(notificationFacade,userSessionRepository,eurekaClientInfo);
        return new UserSessionFacade(userSessionService);
    }
}
